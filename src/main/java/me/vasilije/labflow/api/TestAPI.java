package me.vasilije.labflow.api;

import jakarta.servlet.http.HttpServletRequest;
import me.vasilije.labflow.exception.TypeNotFoundException;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.service.TestService;
import me.vasilije.labflow.utils.TokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TestAPI {

    private final TokenUtils utils;
    private final TestService testService;

    public TestAPI(TestService testService, TokenUtils utils) {
        this.testService = testService;
        this.utils = utils;
    }

    @RequestMapping(path = "/check", method = RequestMethod.GET)
    public ResponseEntity checkPatientTests(HttpServletRequest req) {

        var jwtToken = req.getHeader("Authorization").split(" ")[1];

        try {
            return testService.checkPatientTests(jwtToken);
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }catch (TypeNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }

    @RequestMapping(path = "/check/{id}", method = RequestMethod.GET)
    public ResponseEntity checkTest(@PathVariable long id, HttpServletRequest req) {

        var jwtToken = req.getHeader("Authorization").split(" ")[1];

        try {
            return testService.checkTest(id, jwtToken);
        }catch (TypeNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @RequestMapping(path = "/schedule/{hospitalId}/{id}/{submitTypeId}", method = RequestMethod.POST)
    public ResponseEntity scheduleTest(@PathVariable long hospitalId ,@PathVariable long id, @PathVariable long submitTypeId, HttpServletRequest req) {

        var jwtToken = req.getHeader("Authorization").split(" ")[1];

        if(!utils.stillValid(jwtToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not logged in.");
        }

        try {
            return testService.addTestToQueue(id, submitTypeId, hospitalId, utils.getUsername(jwtToken));
        } catch (TypeNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(503).body(e.getMessage());
        }
    }

}
