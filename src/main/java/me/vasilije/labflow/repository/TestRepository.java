package me.vasilije.labflow.repository;

import me.vasilije.labflow.model.Test;
import me.vasilije.labflow.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

    @Query(value = "SELECT t FROM Test t")
    List<Test> getTests(Pageable pageable);

    List<Test> findByPatient(User patient);
}
