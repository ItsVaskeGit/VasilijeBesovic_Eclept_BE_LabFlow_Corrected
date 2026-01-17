package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import me.vasilije.labflow.repository.MachineRepository;
import me.vasilije.labflow.repository.TechnicianRepository;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MachineService {

    private final MachineRepository machineRepository;
    private final TechnicianRepository technicianRepository;
    private final ScheduledTaskService scheduledTaskService;

    private final TaskScheduler scheduler = new SimpleAsyncTaskScheduler();

    public MachineService(MachineRepository machineRepository, TechnicianRepository technicianRepository,
                          ScheduledTaskService scheduledTaskService) {
        this.machineRepository = machineRepository;
        this.technicianRepository = technicianRepository;
        this.scheduledTaskService = scheduledTaskService;
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
