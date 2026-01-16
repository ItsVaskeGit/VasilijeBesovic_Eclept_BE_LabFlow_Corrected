package me.vasilije.labflow.api;

import jakarta.servlet.http.HttpServletRequest;
import me.vasilije.labflow.exception.NoMachinesAvailableException;
import me.vasilije.labflow.exception.TypeNotFoundException;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.model.Test;
import me.vasilije.labflow.service.MachineService;
import me.vasilije.labflow.service.TestService;
import me.vasilije.labflow.utils.TokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TestAPI {

    private final TokenUtils utils;
    private final TestService testService;
    private final MachineService machineService;

    public TestAPI(TestService testService, MachineService machineService, TokenUtils utils) {
        this.testService = testService;
        this.machineService = machineService;
        this.utils = utils;
    }

    @RequestMapping(path = "/schedule/{id}/{submitTypeId}", method = RequestMethod.POST)
    public ResponseEntity sheduleTest(@PathVariable long id, @PathVariable long submitTypeId, HttpServletRequest req) {

        var jwtToken = req.getHeader("Authorization").split(" ")[1];

        if(!utils.checkToken(jwtToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not logged in.");
        }

        try {
            return testService.addTestToQueue(id, submitTypeId, utils.getUsername(jwtToken));
        } catch (TypeNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(503).body(e.getMessage());
        }
    }
}
