package me.vasilije.labflow.service;

import me.vasilije.labflow.model.LabMachine;
import me.vasilije.labflow.repository.MachineRepository;
import org.springframework.stereotype.Service;

@Service
public class MachineService {

    private final MachineRepository repository;

    public MachineService(MachineRepository repository) {
        this.repository = repository;
    }

    public boolean replaceReagents() {
        // TODO
        return true;
    }

    public LabMachine checkReagentAvailability() {

        var machines = repository.findAll();

        // TODO
        return null;
    }
}
