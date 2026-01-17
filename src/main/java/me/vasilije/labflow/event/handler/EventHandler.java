package me.vasilije.labflow.event.handler;

import me.vasilije.labflow.event.*;
import me.vasilije.labflow.exception.TypeNotFoundException;
import me.vasilije.labflow.repository.QueueEntryRepository;
import me.vasilije.labflow.repository.QueueRepository;
import me.vasilije.labflow.service.MachineService;
import me.vasilije.labflow.service.TestService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventHandler {

    private final MachineService machineService;
    private final TestService testService;
    private final QueueEntryRepository queueEntryRepository;
    private final QueueRepository queueRepository;

    public EventHandler(MachineService machineService, TestService testService,
                        QueueEntryRepository queueEntryRepository, QueueRepository queueRepository) {
        this.machineService = machineService;
        this.testService = testService;
        this.queueEntryRepository = queueEntryRepository;
        this.queueRepository = queueRepository;
    }

    @EventListener
    private void handleResumeEvent(ResumeQueueEvent event) {

        var queue = queueRepository.findById(event.getQueueId()).orElseThrow(() -> new TypeNotFoundException("Queue not found."));

        if(!queue.active) {
            testService.startQueue(event.getHospitalId());
        }
    }

    @EventListener
    private void handleNewTestEvent(NewTestEvent event) {

        var queue = queueRepository.findById(event.getQueueId()).orElseThrow(() -> new TypeNotFoundException("Queue not found."));

        if(!queue.active) {
            testService.startQueue(event.getHospitalId());
        }
    }

    @EventListener
    private void handleStartNewTestEvent(StartNewTestEvent event) {

        var queue = queueRepository.findById(event.getQueueId()).orElseThrow(() -> new TypeNotFoundException("Queue not found."));

        if(queueEntryRepository.countQueueEntryByQueue(queue) > 0) {
            testService.startQueue(event.getHospitalId());
        }
    }

    @EventListener
    private void handleTestFinishedEvent(TestFinishedEvent event) {

        var queue = queueRepository.findById(event.getQueueId()).orElseThrow(() -> new TypeNotFoundException("Queue not found."));

        if(queueEntryRepository.countQueueEntryByQueue(queue) > 0) {
            testService.startQueue(event.getHospitalId());
        }
    }

    @EventListener
    private void handleStartReagentReplacementEvent(StartReagentReplacementEvent event) {
        machineService.replaceReagents(event.getHospitalId());
    }
}
