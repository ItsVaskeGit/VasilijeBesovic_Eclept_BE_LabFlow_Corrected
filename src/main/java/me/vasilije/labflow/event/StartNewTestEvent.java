package me.vasilije.labflow.event;

import org.springframework.context.ApplicationEvent;

public class StartNewTestEvent extends ApplicationEvent {
    public StartNewTestEvent(Object source) {
        super(source);
    }
}
