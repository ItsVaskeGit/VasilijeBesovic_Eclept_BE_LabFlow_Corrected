package me.vasilije.labflow.api;

import jakarta.servlet.http.HttpServletRequest;
import me.vasilije.labflow.exception.NoMachinesAvailableException;
import me.vasilije.labflow.service.MachineService;
import me.vasilije.labflow.service.TestService;
import me.vasilije.labflow.utils.TokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TestAPI {

    private final TokenUtils utils = new TokenUtils();

    private final TestService testService;
    private final MachineService machineService;

    private TestAPI(TestService testService, MachineService machineService) {
        this.testService = testService;
        this.machineService = machineService;
    }

    @RequestMapping(path = "/schedule/{id}", method = RequestMethod.POST)
    public Boolean sheduleTest(@PathVariable long id, HttpServletRequest req) {

        var jwtToken = req.getHeader("Authorization").split(" ")[1];

        if(!utils.checkToken(jwtToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not logged in.");
        }

        try {
            return testService.scheduleTest(id, utils.getUsername(jwtToken));
        } catch (NoMachinesAvailableException e) {
            // If this exception is thrown then all machines are depleted and replacement of the reagents is needed.
            machineService.replaceReagents();
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "All machines are undergoing maintenance now. Try again in a few minutes.");
        }
    }
}
