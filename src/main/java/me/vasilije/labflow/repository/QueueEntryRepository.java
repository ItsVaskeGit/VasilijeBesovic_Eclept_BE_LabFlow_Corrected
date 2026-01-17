package me.vasilije.labflow.repository;

import me.vasilije.labflow.model.Queue;
import me.vasilije.labflow.model.QueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QueueEntryRepository extends JpaRepository<QueueEntry, Long> {

    @Query(value = "SELECT q FROM QueueEntry q ORDER BY q.id DESC")
    Optional<QueueEntry> findFirst();

    Optional<QueueEntry> findTopByQueueOrderByIdAsc(Queue queue);

    double countQueueEntryByQueue(Queue queue);
}
