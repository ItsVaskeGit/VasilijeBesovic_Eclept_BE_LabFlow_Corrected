package me.vasilije.labflow.repository;

import me.vasilije.labflow.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Long> {

    @Query(value = "SELECT t FROM Technician t JOIN t.labMachine m WHERE t.isBusy = false AND m.reagentUnits >= ?1")
    Optional<Technician> findFreeTechnicianWithAvailableMachine(int reagentsNeeded);
}
