package me.vasilije.labflow.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TestFinishedEvent extends ApplicationEvent {

    private final long hospitalId;
    private final long queueId;

    public TestFinishedEvent(Object source, long hospitalId, long queueId) {
        super(source);
        this.hospitalId = hospitalId;
        this.queueId = queueId;
    }
}
