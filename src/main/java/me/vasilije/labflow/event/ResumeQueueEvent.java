package me.vasilije.labflow.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ResumeQueueEvent extends ApplicationEvent {

    private final long hospitalId;
    private final long queueId;

    public ResumeQueueEvent(Object source, long hospitalId, long queueId) {
        super(source);
        this.hospitalId = hospitalId;
        this.queueId = queueId;
    }
}
