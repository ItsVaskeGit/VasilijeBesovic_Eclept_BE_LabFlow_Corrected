package me.vasilije.labflow.service;

import me.vasilije.labflow.model.Test;
import me.vasilije.labflow.repository.TestRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TestService {

    private final TestRepository repository;

    public TestService(TestRepository repository) {
        this.repository = repository;
    }

    public boolean scheduleTest() {
        // TODO
        return true;
    }

    public Optional<Test> findTestById(long id) {
        return repository.findById(id);
    }
}
