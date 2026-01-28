package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.vasilije.labflow.event.ResumeQueueEvent;
import me.vasilije.labflow.event.TestFinishedEvent;
import me.vasilije.labflow.exception.TypeNotFoundException;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.model.LabMachine;
import me.vasilije.labflow.repository.HospitalRepository;
import me.vasilije.labflow.repository.QueueRepository;
import me.vasilije.labflow.repository.TechnicianRepository;
import me.vasilije.labflow.repository.TestRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

    private final TestRepository testRepository;
    private final TechnicianRepository technicianRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final QueueRepository queueRepository;
    private final HospitalRepository hospitalRepository;

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
