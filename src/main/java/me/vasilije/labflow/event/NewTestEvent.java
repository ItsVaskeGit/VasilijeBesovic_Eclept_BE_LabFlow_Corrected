package me.vasilije.labflow.event;

import org.springframework.context.ApplicationEvent;

public class NewTestEvent extends ApplicationEvent {
    public NewTestEvent(Object source) {
        super(source);
    }
}
