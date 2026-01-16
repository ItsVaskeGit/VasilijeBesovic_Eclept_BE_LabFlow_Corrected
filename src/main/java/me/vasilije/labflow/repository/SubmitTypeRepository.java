package me.vasilije.labflow.repository;

import me.vasilije.labflow.model.SubmitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmitTypeRepository extends JpaRepository<SubmitType, Long> {
}
