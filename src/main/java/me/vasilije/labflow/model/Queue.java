package me.vasilije.labflow.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public boolean active;

    @OneToMany(mappedBy = "queue")
    public List<QueueEntry> entries;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hospital_id", referencedColumnName = "id")
    public Hospital hospital;

}
