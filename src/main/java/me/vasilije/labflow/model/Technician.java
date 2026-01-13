package me.vasilije.labflow.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Technician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public boolean isBusy;

    @ManyToOne
    public User user;
}
