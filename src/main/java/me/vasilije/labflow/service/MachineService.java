package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import me.vasilije.labflow.model.LabMachine;
import me.vasilije.labflow.model.Technician;
import me.vasilije.labflow.repository.MachineRepository;
import me.vasilije.labflow.repository.TechnicianRepository;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MachineService {

    private final MachineRepository machineRepository;
    private final TechnicianRepository technicianRepository;

    private final TaskScheduler scheduler = new SimpleAsyncTaskScheduler();

    public MachineService(MachineRepository machineRepository, TechnicianRepository technicianRepository) {
        this.machineRepository = machineRepository;
        this.technicianRepository = technicianRepository;
    }

    @Transactional
    public void replaceReagents() {
        var technicians = technicianRepository.findAll();
        for(var technician : technicians) {
            technician.setBusy(true);
        }
        scheduler.schedule(doReplacement(), Instant.now().plusSeconds(240));
    }

    @Transactional
    Runnable doReplacement() {
        return () -> {

            var machines = machineRepository.findAll();
            var technicians = technicianRepository.findAll();

            for(var machine : machines) {
                machine.setReagentUnits(500);
            }

            for(var technician : technicians) {
                technician.setBusy(false);
            }
        };
    }
}
