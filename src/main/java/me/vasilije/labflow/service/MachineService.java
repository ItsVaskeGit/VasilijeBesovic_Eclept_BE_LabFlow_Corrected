package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.vasilije.labflow.dto.response.MachineDTO;
import me.vasilije.labflow.exception.TypeNotFoundException;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.model.LabMachine;
import me.vasilije.labflow.repository.HospitalRepository;
import me.vasilije.labflow.repository.MachineRepository;
import me.vasilije.labflow.repository.TechnicianRepository;
import me.vasilije.labflow.repository.UserRepository;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MachineService {

    private final MachineRepository machineRepository;
    private final TechnicianRepository technicianRepository;
    private final ScheduledTaskService scheduledTaskService;
    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;

    private final TaskScheduler scheduler = new SimpleAsyncTaskScheduler();

    @Transactional
    public MachineDTO createMachine() {

        var newMachine = new LabMachine();

        newMachine.setReagentUnits(500);

        var savedMachine = machineRepository.save(newMachine);

        return new MachineDTO(savedMachine.getId(), savedMachine.getReagentUnits());
    }

    @Transactional
    public MachineDTO assignMachine(long machineId, long userId) {

        var machine = machineRepository.findById(machineId).orElseThrow(() -> new TypeNotFoundException("Machine was not found."));

        var technicianUser = userRepository.findById(userId).orElseThrow(() -> new TypeNotFoundException("User was not found."));

        var technician = technicianRepository.findByUser(technicianUser).orElseThrow(() -> new UserNotFoundException("User is not a technician."));

        technician.setLabMachine(machine);

        return new MachineDTO(machine.getId(), machine.getReagentUnits());
    }

    @Transactional
    public void replaceReagents(long hospitalId) {

        var hospital = hospitalRepository.findById(hospitalId).orElseThrow(() -> new TypeNotFoundException("Hospital not found."));

        var technicians = technicianRepository.findByHospital(hospital).orElseThrow(() -> new TypeNotFoundException("No technicians found at that hospital."));

        var machines = new ArrayList<LabMachine>();

        for(var technician : technicians) {
            technician.setBusy(true);
            machines.add(machineRepository.findByTechnician(technician));
        }

        for(var machine: machines) {
            machine.setUnderMaintenance(true);
        }

        scheduler.schedule(() -> scheduledTaskService.startReagentReplacement(hospitalId), Instant.now().plusSeconds(240));
    }

}
