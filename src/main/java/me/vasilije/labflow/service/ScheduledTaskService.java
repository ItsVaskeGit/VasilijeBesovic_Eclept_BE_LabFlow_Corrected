package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import me.vasilije.labflow.event.ResumeQueueEvent;
import me.vasilije.labflow.event.TestFinishedEvent;
import me.vasilije.labflow.exception.TypeNotFoundException;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.model.Hospital;
import me.vasilije.labflow.model.LabMachine;
import me.vasilije.labflow.repository.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduledTaskService {

    private final TestRepository testRepository;
    private final MachineRepository machineRepository;
    private final TechnicianRepository technicianRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final QueueEntryRepository queueEntryRepository;
    private final QueueRepository queueRepository;
    private final HospitalRepository hospitalRepository;

    public ScheduledTaskService(MachineRepository machineRepository, TechnicianRepository technicianRepository,
                                TestRepository testRepository, ApplicationEventPublisher applicationEventPublisher,
                                QueueEntryRepository queueEntryRepository, QueueRepository queueRepository,
                                HospitalRepository hospitalRepository) {
        this.machineRepository = machineRepository;
        this.technicianRepository = technicianRepository;
        this.testRepository = testRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.queueEntryRepository = queueEntryRepository;
        this.queueRepository = queueRepository;
        this.hospitalRepository = hospitalRepository;
    }

    @Transactional
    public void startReagentReplacement(long hospitalId) {

        var hospital = hospitalRepository.findById(hospitalId).orElseThrow(() -> new TypeNotFoundException("Hospital not found"));

        var technicians = technicianRepository.findByHospital(hospital).orElseThrow(() -> new TypeNotFoundException("There are no technicians at that hospital"));

        var machines = new ArrayList<LabMachine>();

        for(var technician : technicians) {
            technician.setBusy(false);
            machines.add(technician.labMachine);
        }

        for(var machine : machines) {
            machine.setUnderMaintenance(false);
            machine.setReagentUnits(500);
        }


        applicationEventPublisher.publishEvent(new ResumeQueueEvent(this, hospitalId, hospital.queue.getId()));
    }

    @Transactional
    public void finishTest(long testId, long technicianId) {

        var test = testRepository.findById(testId).get();
        var technician = technicianRepository.findById(technicianId).orElseThrow(() -> new UserNotFoundException("Technician was not found"));
        var queue = queueRepository.findByHospital(technician.hospital).orElseThrow((() -> new TypeNotFoundException("Queue was not found.")));

        test.setFinished(true);
        technician.setBusy(false);

        applicationEventPublisher.publishEvent(new TestFinishedEvent(this, technician.hospital.getId(), queue.getId(), test));
    }
}
