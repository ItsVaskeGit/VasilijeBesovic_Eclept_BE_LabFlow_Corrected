package me.vasilije.labflow.event.handler;

import me.vasilije.labflow.event.*;
import me.vasilije.labflow.repository.QueueEntryRepository;
import me.vasilije.labflow.service.MachineService;
import me.vasilije.labflow.service.TestService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventHandler {

    private final MachineService machineService;
    private final TestService testService;
    private final QueueEntryRepository queueEntryRepository;

    public EventHandler(MachineService machineService, TestService testService,
                        QueueEntryRepository queueEntryRepository) {
        this.machineService = machineService;
        this.testService = testService;
        this.queueEntryRepository = queueEntryRepository;
    }

    @EventListener
    private void handleResumeEvent(ResumeQueueEvent event) {
        if(queueEntryRepository.count() > 0) {
            testService.startQueue();
        }
    }

    @EventListener
    private void handleNewTestEvent(NewTestEvent event) {
        if(!testService.isQueueActive) {
            testService.startQueue();
        }
    }

    @EventListener
    private void handleStartNewTestEvent(StartNewTestEvent event) {
        if(queueEntryRepository.count() > 0) {
            testService.startQueue();
        }
    }

    @EventListener
    private void handleTestFinishedEvent(TestFinishedEvent event) {
        if(queueEntryRepository.count() > 0) {
            testService.startQueue();
        }
    }

    @EventListener
    private void handleStartReagentReplacementEvent(StartReagentReplacementEvent event) {
        machineService.replaceReagents();
    }
}
