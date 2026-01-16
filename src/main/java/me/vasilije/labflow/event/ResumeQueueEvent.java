package me.vasilije.labflow.event;

import org.springframework.context.ApplicationEvent;

public class ResumeQueueEvent extends ApplicationEvent {

    public ResumeQueueEvent(Object source) {
        super(source);
    }
}
