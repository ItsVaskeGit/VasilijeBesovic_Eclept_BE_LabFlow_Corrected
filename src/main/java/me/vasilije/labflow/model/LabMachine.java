package me.vasilije.labflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class LabMachine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int reagentUnits;

    @JsonIgnore
    @OneToOne(mappedBy = "labMachine")
    private Technician technician;

    private boolean underMaintenance;
}
