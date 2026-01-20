package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import me.vasilije.labflow.event.NewTestEvent;
import me.vasilije.labflow.event.StartReagentReplacementEvent;
import me.vasilije.labflow.exception.NoMachinesAvailableException;
import me.vasilije.labflow.exception.TypeNotFoundException;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.model.QueueEntry;
import me.vasilije.labflow.model.Test;
import me.vasilije.labflow.model.TestType;
import me.vasilije.labflow.repository.*;
import me.vasilije.labflow.utils.TokenUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class TestService {

    private final TestRepository testRepository;
    private final TestTypeRepository typeRepository;
    private final TechnicianRepository technicianRepository;
    private final UserRepository userRepository;
    private final MachineRepository machineRepository;
    private final ScheduledTaskService scheduledTaskService;
    private final SubmitTypeRepository submitTypeRepository;
    private final QueueEntryRepository queueEntryRepository;
    private final QueueRepository queueRepository;
    private final HospitalRepository hospitalRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final TokenUtils utils;

    private final TaskScheduler scheduler = new SimpleAsyncTaskScheduler();

    public TestService(TestRepository testRepository, TechnicianRepository technicianRepository,
                       MachineRepository machineRepository, TestTypeRepository typeRepository,
                       UserRepository userRepository, ScheduledTaskService scheduledTaskService,
                       SubmitTypeRepository submitTypeRepository, QueueEntryRepository queueEntryRepository,
                       ApplicationEventPublisher eventPublisher, QueueRepository queueRepository,
                       HospitalRepository hospitalRepository, TokenUtils utils) {
        this.testRepository = testRepository;
        this.technicianRepository = technicianRepository;
        this.machineRepository = machineRepository;
        this.typeRepository = typeRepository;
        this.userRepository = userRepository;
        this.scheduledTaskService = scheduledTaskService;
        this.submitTypeRepository = submitTypeRepository;
        this.queueEntryRepository = queueEntryRepository;
        this.eventPublisher = eventPublisher;
        this.queueRepository = queueRepository;
        this.hospitalRepository = hospitalRepository;
        this.utils = utils;
    }

    public ResponseEntity checkTest(long id, String jwtToken) {

        if(!utils.stillValid(jwtToken)) {
            return ResponseEntity.status(401).body("You are not logged in or your session has expired.");
        }

        var user = userRepository.findByUsername(utils.getUsername(jwtToken)).orElseThrow(() -> new UserNotFoundException("User not found."));
        var test = testRepository.findById(id).orElseThrow(() -> new TypeNotFoundException("Test with given id was not found."));

        if(!(test.patient.getId() == user.getId())) {
            return ResponseEntity.status(401).body("This test does not belong to you.");
        }

        return ResponseEntity.status(200).body(test);
    }

    public ResponseEntity checkPatientTests(String jwtToken) {

        if(!utils.stillValid(jwtToken)) {
            return ResponseEntity.status(401).body("You are not logged in or your session has expired.");
        }

        var user = userRepository.findByUsername(utils.getUsername(jwtToken)).orElseThrow(() -> new UserNotFoundException("User not found."));
        var tests = testRepository.findByPatient(user);

        return ResponseEntity.status(200).body(tests);
    }

    public ResponseEntity getTestsPaginate(int pageNumber, int perPage, String jwtToken) {

        if(!utils.stillValid(jwtToken)) {
            return ResponseEntity.status(401).body("You are not logged in or your session has expired.");
        }

        var user = userRepository.findByUsername(utils.getUsername(jwtToken)).orElseThrow(() -> new UserNotFoundException("User not found."));

        if(!user.isAdmin()) {
            return ResponseEntity.status(401).body("You don't have necessary permissions to access this.");
        }

        return ResponseEntity.status(200).body(testRepository.getTests(PageRequest.of(pageNumber, perPage)));
    }

    public ResponseEntity addTestToQueue(long testId, long submitTypeId, long hospitalId, String username) {

        var submitType = submitTypeRepository.findById(submitTypeId).orElseThrow(() -> new TypeNotFoundException("Submit type not found."));

        var testType = typeRepository.findById(testId).orElseThrow(() -> new TypeNotFoundException("Test type not found"));

        var hospital = hospitalRepository.findById(hospitalId).orElseThrow(() -> new TypeNotFoundException("Hospital not found"));

        var queue = queueRepository.findByHospital(hospital).orElseThrow(() -> new TypeNotFoundException("That hospital does not have a queue."));

        if(!submitType.isPriority() && queueEntryRepository.countQueueEntryByQueue(queue) >= 20) {
            return ResponseEntity.status(503).body("Queue is full. Try again later.");
        }

        if(machineRepository.allMachinesUnderMaintenance(hospitalId)) {
            return ResponseEntity.status(503).body("All machines are currently undergoing maintenance.");
        }

        if(machineRepository.allMachinesDepleted(hospitalId)) {
            eventPublisher.publishEvent(new StartReagentReplacementEvent(this, hospitalId));
            return ResponseEntity.status(503).body("All machines are currently undergoing maintenance.");
        }

        var entry = new QueueEntry();

        entry.setType(testType);
        entry.setPatient(userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User does not exist.")));
        entry.setQueue(queue);

        var savedEntry = queueEntryRepository.save(entry);

        eventPublisher.publishEvent(new NewTestEvent(this, hospitalId, queue.getId()));

        return ResponseEntity.status(200).body(savedEntry);
    }

    @Transactional
    public void startQueue(long hospitalId) {

        var hospital = hospitalRepository.findById(hospitalId).orElseThrow(() -> new TypeNotFoundException("Hospital does not exist."));

        var queue = queueRepository.findByHospital(hospital).orElseThrow(() -> new TypeNotFoundException("Queue was not found"));

        var nextTest = queueEntryRepository.findTopByQueueOrderByIdAsc(queue).orElseThrow(() -> new TypeNotFoundException("Next queued test not found."));
        var testType = typeRepository.findById(nextTest.type.getId()).orElseThrow(() -> new TypeNotFoundException("Next test type not found."));

        if(!(technicianRepository.countReadyTechnicians(testType.getReagentUnitsNeeded(), hospitalId) == 0)) {

            queue.setActive(true);

            var patient = userRepository.findById(nextTest.patient.getId()).orElseThrow(() -> new UserNotFoundException("Patient was not found."));

            scheduleTest(testType, hospitalId, nextTest.getId(), patient.getUsername());

        }else {
            queue.setActive(false);
            eventPublisher.publishEvent(new StartReagentReplacementEvent(this, hospitalId));
        }

    }

    @Transactional
    public void scheduleTest(TestType type, long hospitalId, long queueId, String username) throws ResponseStatusException, NoMachinesAvailableException {

        var availableTechnician = technicianRepository.findFreeTechniciansWithAvailableMachines(type.getReagentUnitsNeeded(), hospitalId)
                .orElseThrow(() -> new NoMachinesAvailableException("There are no machines available now.")).getFirst();

        var availableMachine = machineRepository.getByTechnician(availableTechnician)
                .orElseThrow(() -> new TypeNotFoundException("Error, machine not found."));

        var patient = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        var queueEntry = queueEntryRepository.findById(queueId).orElseThrow(() -> new TypeNotFoundException("Queue entry not found."));

        var newTest = new Test();

        newTest.setType(type);
        newTest.setTechnician(availableTechnician);
        newTest.setMachine(availableMachine);
        newTest.setPatient(patient);
        newTest.setFinished(false);

        availableTechnician.setBusy(true);

        availableMachine.setReagentUnits(availableMachine.getReagentUnits() - type.getReagentUnitsNeeded());

        var savedTest = testRepository.save(newTest);

        queueEntryRepository.delete(queueEntry);

        scheduler.schedule(() -> scheduledTaskService.finishTest(savedTest.getId(), availableTechnician.getId()),
                Instant.now().plusSeconds(type.getDuration()));
    }
}
