package me.vasilije.labflow.event;

import lombok.Getter;
import me.vasilije.labflow.model.Test;
import org.springframework.context.ApplicationEvent;

@Getter
public class TestFinishedEvent extends ApplicationEvent {

    private final long hospitalId;
    private final long queueId;
    private final Test test;

    public TestFinishedEvent(Object source, long hospitalId, long queueId, Test test) {
        super(source);
        this.hospitalId = hospitalId;
        this.queueId = queueId;
        this.test = test;
    }
}
