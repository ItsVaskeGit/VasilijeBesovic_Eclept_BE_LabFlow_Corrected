package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import me.vasilije.labflow.exception.TypeNotFoundException;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.model.LabMachine;
import me.vasilije.labflow.repository.MachineRepository;
import me.vasilije.labflow.repository.TechnicianRepository;
import me.vasilije.labflow.repository.UserRepository;
import me.vasilije.labflow.utils.TokenUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MachineService {

    private final MachineRepository machineRepository;
    private final TechnicianRepository technicianRepository;
    private final ScheduledTaskService scheduledTaskService;
    private final UserRepository userRepository;
    private final TokenUtils utils;

    private final TaskScheduler scheduler = new SimpleAsyncTaskScheduler();

    public MachineService(MachineRepository machineRepository, TechnicianRepository technicianRepository,
                          ScheduledTaskService scheduledTaskService, UserRepository userRepository, TokenUtils utils) {
        this.machineRepository = machineRepository;
        this.technicianRepository = technicianRepository;
        this.scheduledTaskService = scheduledTaskService;
        this.userRepository = userRepository;
        this.utils = utils;
    }

    @Transactional
    public ResponseEntity createMachine(String jwtToken) {

        var user = userRepository.findByUsername(utils.getUsername(jwtToken)).orElseThrow(() -> new UserNotFoundException("User not found."));

        if(!user.isAdmin()) {
            return ResponseEntity.status(401).body("You don't have necessary permissions to access this.");
        }

        var newMachine = new LabMachine();

        newMachine.setReagentUnits(500);

        return ResponseEntity.status(200).body(machineRepository.save(newMachine));
    }

    @Transactional
    public ResponseEntity assignMachine(long machineId, long userId, String jwtToken) {

        var user = userRepository.findByUsername(utils.getUsername(jwtToken)).orElseThrow(() -> new UserNotFoundException("User not found."));

        if(!user.isAdmin()) {
            return ResponseEntity.status(401).body("You don't have necessary permissions to access this.");
        }

        var machine = machineRepository.findById(machineId).orElseThrow(() -> new TypeNotFoundException("Machine was not found."));

        var technicianUser = userRepository.findById(userId).orElseThrow(() -> new TypeNotFoundException("User was not found."));

        var technician = technicianRepository.findByUser(technicianUser).orElseThrow(() -> new UserNotFoundException("User is not a technician."));

        technician.setLabMachine(machine);

        return ResponseEntity.status(200).body(machine);
    }

    @Transactional
    public void replaceReagents(long hospitalId) {
        var technicians = technicianRepository.findAll();
        for(var technician : technicians) {
            technician.setBusy(true);
        }

        scheduler.schedule(() -> scheduledTaskService.startReagentReplacement(hospitalId), Instant.now().plusSeconds(240));
    }

}
