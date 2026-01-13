package me.vasilije.labflow.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private int duration;

    private int reagentUnitsNeeded;

    @ManyToOne
    public User patient;
}
