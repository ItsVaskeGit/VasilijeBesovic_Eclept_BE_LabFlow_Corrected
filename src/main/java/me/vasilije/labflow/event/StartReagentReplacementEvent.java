package me.vasilije.labflow.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class StartReagentReplacementEvent extends ApplicationEvent {

    private final long hospitalId;

    public StartReagentReplacementEvent(Object source, long hospitalId) {
        super(source);
        this.hospitalId = hospitalId;
    }
}
