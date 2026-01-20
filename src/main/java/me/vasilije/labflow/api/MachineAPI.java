package me.vasilije.labflow.api;

import jakarta.servlet.http.HttpServletRequest;
import me.vasilije.labflow.exception.TypeNotFoundException;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.service.MachineService;
import me.vasilije.labflow.utils.TokenUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MachineAPI {

    private final MachineService machineService;
    private final TokenUtils utils;

    public MachineAPI(MachineService machineService, TokenUtils utils) {
        this.machineService = machineService;
        this.utils = utils;
    }

    @RequestMapping(path = "/machine", method = RequestMethod.POST)
    public ResponseEntity createMachine(HttpServletRequest req) {

        if(!utils.requestHasToken(req)) {
            return ResponseEntity.status(401).body("You need to provide authentication credentials.");
        }

        if (!utils.stillValid(utils.getToken(req))) {
            return ResponseEntity.status(401).body("Your session has expired.");
        }

        try {
            return machineService.createMachine(utils.getToken(req));
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @RequestMapping(path = "/assign/{userId}/{machineId}", method = RequestMethod.PUT)
    public ResponseEntity assignMachine(@PathVariable long userId, @PathVariable long machineId, HttpServletRequest req) {

        if(!utils.requestHasToken(req)) {
            return ResponseEntity.status(401).body("You need to provide authentication credentials.");
        }

        if (!utils.stillValid(utils.getToken(req))) {
            return ResponseEntity.status(401).body("Your session has expired.");
        }

        try {
            return machineService.assignMachine(machineId, userId, utils.getToken(req));
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }catch (TypeNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
