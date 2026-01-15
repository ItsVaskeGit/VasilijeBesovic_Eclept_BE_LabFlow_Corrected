package me.vasilije.labflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private TestType type;

    @ManyToOne
    @JsonIgnore
    public User patient;

    @ManyToOne
    public Technician technician;

    @ManyToOne
    public LabMachine machine;

    private boolean finished;

}
