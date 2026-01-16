package me.vasilije.labflow.repository;

import me.vasilije.labflow.model.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QueueRepository extends JpaRepository<Queue, Long> {

    long countAll();

    @Query(value = "SELECT q FROM Queue q ORDER BY q.id DESC")
    Optional<Queue> findFirst();
}
