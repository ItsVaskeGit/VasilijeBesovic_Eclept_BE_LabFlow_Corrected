package me.vasilije.labflow.event;

import org.springframework.context.ApplicationEvent;

public class StartReagentReplacementEvent extends ApplicationEvent {
    public StartReagentReplacementEvent(Object source) {
        super(source);
    }
}
