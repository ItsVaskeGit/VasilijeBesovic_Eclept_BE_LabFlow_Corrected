package me.vasilije.labflow.repository;

import me.vasilije.labflow.model.LabMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends JpaRepository<LabMachine, Long> {
}
