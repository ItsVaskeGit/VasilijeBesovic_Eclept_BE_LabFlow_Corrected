package me.vasilije.labflow.repository;

import me.vasilije.labflow.model.LabMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepository<LabMachine, Long> {

    @Query(value = "SELECT m FROM LabMachine m WHERE m.reagentUnits >= ?1 ORDER BY m.id LIMIT 1")
    Optional<LabMachine> findImmediatelyAvailableMachine(int reagentsNeeded);
}
