package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import me.vasilije.labflow.exception.NoMachinesAvailableException;
import me.vasilije.labflow.model.Technician;
import me.vasilije.labflow.model.Test;
import me.vasilije.labflow.model.TestType;
import me.vasilije.labflow.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

@Service
public class TestService {

    private final TestRepository testRepository;
    private final TestTypeRepository typeRepository;
    private final TechnicianRepository technicianRepository;
    private final UserRepository userRepository;
    private final MachineRepository machineRepository;

    private final TaskScheduler scheduler = new SimpleAsyncTaskScheduler();

    public TestService(TestRepository testRepository, TechnicianRepository technicianRepository,
                       MachineRepository machineRepository, TestTypeRepository typeRepository,
                       UserRepository userRepository) {
        this.testRepository = testRepository;
        this.technicianRepository = technicianRepository;
        this.machineRepository = machineRepository;
        this.typeRepository = typeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean scheduleTest(long testId, String username) throws ResponseStatusException, NoMachinesAvailableException {

        var type = findTestById(testId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "That type of test does not exist in the system."));

        var availableTechnician = technicianRepository.findFreeTechnicianWithAvailableMachine(type.getReagentUnitsNeeded())
                .orElseThrow(() -> new NoMachinesAvailableException("There are no machines available now."));

        var availableMachine = availableTechnician.labMachine;

        var patient = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found."));

        var newTest = new Test();

        newTest.setType(type);
        newTest.setTechnician(availableTechnician);
        newTest.setMachine(availableMachine);
        newTest.setPatient(patient);
        newTest.setFinished(false);

        availableTechnician.setBusy(true);

        availableMachine.setReagentUnits(availableMachine.getReagentUnits() - type.getReagentUnitsNeeded());

        scheduler.schedule(doATest(newTest, availableTechnician), Instant.now().plusSeconds(type.getDuration()));

        return true;
    }


    public Optional<TestType> findTestById(long id) {
        return typeRepository.findById(id);
    }

    private Runnable doATest(Test test, Technician technician) {
        return () -> {
            test.setFinished(true);
            technician.setBusy(false);
        };
    }
}
