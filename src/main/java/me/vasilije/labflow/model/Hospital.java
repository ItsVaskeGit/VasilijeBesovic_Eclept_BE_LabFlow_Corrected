package me.vasilije.labflow.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String name;

    @OneToMany(mappedBy = "hospital")
    public List<Technician> technicians;

    @OneToOne(mappedBy = "hospital")
    public Queue queue;
}
