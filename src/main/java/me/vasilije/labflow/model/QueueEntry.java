package me.vasilije.labflow.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QueueEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    public TestType type;

    @ManyToOne
    public User patient;

    @ManyToOne
    @JoinColumn(name = "queue_id", nullable = false)
    public Queue queue;

}
