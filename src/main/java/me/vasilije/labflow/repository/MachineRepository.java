package me.vasilije.labflow.repository;

import me.vasilije.labflow.model.LabMachine;
import me.vasilije.labflow.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepository<LabMachine, Long> {

    Optional<LabMachine> getByTechnician(Technician technician);

    @Query(value = "SELECT CASE WHEN (COUNT(m) = 0) THEN TRUE ELSE FALSE END " +
            "FROM LabMachine m JOIN Technician t on t.labMachine = m WHERE m.underMaintenance = false AND t.hospital.id = ?1")
    boolean allMachinesUnderMaintenance(long hospitalId);

    @Query(value = "SELECT CASE WHEN (COUNT(m) = 0) THEN TRUE ELSE FALSE END " +
            "FROM LabMachine m JOIN Technician t on t.labMachine = m WHERE m.reagentUnits >= (SELECT MIN(t.reagentUnitsNeeded) FROM TestType t)" +
            "AND t.hospital.id = ?1")
    boolean allMachinesDepleted(long hospitalId);

    LabMachine findByTechnician(Technician technician);
}
