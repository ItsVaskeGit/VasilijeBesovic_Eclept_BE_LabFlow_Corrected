package me.vasilije.labflow.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    public Test test;

}
