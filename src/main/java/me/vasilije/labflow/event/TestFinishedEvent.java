package me.vasilije.labflow.event;

import lombok.Data;
import lombok.Getter;
import me.vasilije.labflow.model.Queue;
import org.springframework.context.ApplicationEvent;

@Getter
public class TestFinishedEvent extends ApplicationEvent {

    private final Queue queue;

    public TestFinishedEvent(Object source, Queue queue) {
        super(source);
        this.queue = queue;
    }

}
