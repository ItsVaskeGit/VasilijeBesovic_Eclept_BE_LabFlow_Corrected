package me.vasilije.labflow.event;

import org.springframework.context.ApplicationEvent;

public class TestFinishedEvent extends ApplicationEvent {
    public TestFinishedEvent(Object source) {
        super(source);
    }
}
