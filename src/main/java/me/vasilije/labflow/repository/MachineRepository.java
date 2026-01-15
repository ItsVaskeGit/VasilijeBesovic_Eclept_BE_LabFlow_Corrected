package me.vasilije.labflow.repository;

import me.vasilije.labflow.model.LabMachine;
import me.vasilije.labflow.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepository<LabMachine, Long> {

    Optional<LabMachine> getByTechnician(Technician technician);
}
