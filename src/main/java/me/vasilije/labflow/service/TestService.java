package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.vasilije.labflow.dto.response.QueueEntryDTO;
import me.vasilije.labflow.dto.response.TestDTO;
import me.vasilije.labflow.dto.response.TestTypeDTO;
import me.vasilije.labflow.event.NewTestEvent;
import me.vasilije.labflow.event.StartReagentReplacementEvent;
import me.vasilije.labflow.exception.NoMachinesAvailableException;
import me.vasilije.labflow.exception.TypeNotFoundException;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.model.QueueEntry;
import me.vasilije.labflow.model.Test;
import me.vasilije.labflow.model.TestType;
import me.vasilije.labflow.repository.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
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

    private final TaskScheduler scheduler = new SimpleAsyncTaskScheduler();

    public TestDTO checkTest(long id, String username) {

        var user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found."));
        var test = testRepository.findById(id).orElseThrow(() -> new TypeNotFoundException("Test with given id was not found."));

        if(!(test.patient.getId() == user.getId())) {
            return null;
        }

        return new TestDTO(test.getId(), test.getSubmitType(), test.getType(), test.isFinished());
    }

    public List<TestDTO> checkPatientTests(String username) {

        var user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found."));

        return testRepository.findByPatient(user).stream()
                .map(test -> new TestDTO(test.getId(), test.getSubmitType(), test.getType(), test.isFinished())).toList();
    }

    public List<TestDTO> getTestsPaginate(int pageNumber, int perPage) {
        return testRepository.getTests(PageRequest.of(pageNumber, perPage)).stream()
                .map((test -> new TestDTO(test.getId(), test.getSubmitType(), test.getType(), test.isFinished()))).toList();
    }

    public QueueEntryDTO addTestToQueue(long testId, long submitTypeId, long hospitalId, String username) {

        var submitType = submitTypeRepository.findById(submitTypeId).orElseThrow(() -> new TypeNotFoundException("Submit type not found."));

        var testType = typeRepository.findById(testId).orElseThrow(() -> new TypeNotFoundException("Test type not found"));

        var hospital = hospitalRepository.findById(hospitalId).orElseThrow(() -> new TypeNotFoundException("Hospital not found"));

        var queue = queueRepository.findByHospital(hospital).orElseThrow(() -> new TypeNotFoundException("That hospital does not have a queue."));

        if(!submitType.isPriority() && queueEntryRepository.countQueueEntryByQueue(queue) >= 20) {
            return null;
        }

        if(machineRepository.allMachinesUnderMaintenance(hospitalId)) {
            return null;
        }

        if(machineRepository.allMachinesDepleted(hospitalId)) {
            eventPublisher.publishEvent(new StartReagentReplacementEvent(this, hospitalId));
            return null;
        }

        var entry = new QueueEntry();

        entry.setType(testType);
        entry.setPatient(userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User does not exist.")));
        entry.setQueue(queue);

        var savedEntry = queueEntryRepository.save(entry);

        eventPublisher.publishEvent(new NewTestEvent(this, hospitalId, queue.getId()));

        return new QueueEntryDTO(savedEntry.getId(), savedEntry.getType());
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
