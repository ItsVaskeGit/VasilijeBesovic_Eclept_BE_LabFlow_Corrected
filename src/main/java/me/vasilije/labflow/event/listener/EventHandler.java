package me.vasilije.labflow.event.listener;

import me.vasilije.labflow.event.*;
import me.vasilije.labflow.service.MachineService;
import me.vasilije.labflow.service.TestService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventHandler {

    private final MachineService machineService;
    private final TestService testService;

    public EventHandler(MachineService machineService, TestService testService) {
        this.machineService = machineService;
        this.testService = testService;
    }

    @EventListener
    private void handleResumeEvent(ResumeQueueEvent event) {
        testService.startQueue();
    }

    @EventListener
    private void handleNewTestEvent(NewTestEvent event) {

    }

    @EventListener
    private void handleStartNewTestEvent(StartNewTestEvent event) {

    }

    @EventListener
    private void handleTestFinishedEvent(TestFinishedEvent event) {

    }

    @EventListener
    private void handleStartReagentReplacementEvent(StartReagentReplacementEvent event) {
        machineService.replaceReagents();
    }
}
