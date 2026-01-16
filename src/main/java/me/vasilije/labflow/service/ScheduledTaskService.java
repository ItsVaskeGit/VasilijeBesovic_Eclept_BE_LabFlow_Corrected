package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import me.vasilije.labflow.event.ResumeQueueEvent;
import me.vasilije.labflow.model.Technician;
import me.vasilije.labflow.model.Test;
import me.vasilije.labflow.repository.MachineRepository;
import me.vasilije.labflow.repository.TechnicianRepository;
import me.vasilije.labflow.repository.TestRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTaskService {

    private final TestRepository testRepository;
    private final MachineRepository machineRepository;
    private final TechnicianRepository technicianRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ScheduledTaskService(MachineRepository machineRepository, TechnicianRepository technicianRepository,
                                TestRepository testRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.machineRepository = machineRepository;
        this.technicianRepository = technicianRepository;
        this.testRepository = testRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public void startReagentReplacement() {
        var machines = machineRepository.findAll();
        var technicians = technicianRepository.findAll();

        for(var machine : machines) {
            machine.setReagentUnits(500);
        }

        for(var technician : technicians) {
            technician.setBusy(false);
        }

        applicationEventPublisher.publishEvent(new ResumeQueueEvent(this));
    }

    @Transactional
    public void finishTest(long testId, long technicianId) {

        var test = testRepository.findById(testId).get();
        var technician = technicianRepository.findById(technicianId).get();

        test.setFinished(true);
        technician.setBusy(false);
    }
}
