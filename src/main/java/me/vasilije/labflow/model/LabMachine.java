package me.vasilije.labflow.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class LabMachine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int reagentUnits;

    @OneToOne(mappedBy = "labMachine")
    private Technician technician;
}
