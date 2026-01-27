package me.vasilije.labflow.api;

import jakarta.servlet.http.HttpServletRequest;
import me.vasilije.labflow.dto.request.TestTypeDTO;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.service.TestService;
import me.vasilije.labflow.service.TestTypeService;
import me.vasilije.labflow.utils.TokenUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminAPI {

    private final TestTypeService testTypeService;
    private final TestService testService;
    private final TokenUtils utils;

    public AdminAPI(TestTypeService testTypeService, TestService testService, TokenUtils utils) {
        this.testTypeService = testTypeService;
        this.testService = testService;
        this.utils = utils;
    }

    @RequestMapping(path = "/test-type", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody TestTypeDTO newTestType, HttpServletRequest req) {

        if(!utils.requestHasToken(req)) {
            return ResponseEntity.status(401).body("You need to provide authentication credentials.");
        }

        try {
            return testTypeService.createNewTestType(newTestType, utils.getToken(req));
        }catch(UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @RequestMapping(path = "/test-type", method = RequestMethod.PUT)
    public ResponseEntity modify(@RequestBody TestTypeDTO modifiedTestType, HttpServletRequest req) {

        if(!utils.requestHasToken(req)) {
            return ResponseEntity.status(401).body("You need to provide authentication credentials.");
        }

        try {
            return testTypeService.modifyTestType(modifiedTestType, utils.getToken(req));
        }catch(UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @RequestMapping(path = "/test-type/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable long id, HttpServletRequest req) {

        if(!utils.requestHasToken(req)) {
            return ResponseEntity.status(401).body("You need to provide authentication credentials.");
        }

        try {
            return testTypeService.deleteTestType(id, utils.getToken(req));
        }catch(UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @RequestMapping(path = "/tests/{pageNumber}/{perPage}", method = RequestMethod.GET)
    public ResponseEntity getTests(@PathVariable int pageNumber, @PathVariable int perPage, HttpServletRequest req) {

        if(!utils.requestHasToken(req)) {
            return ResponseEntity.status(401).body("You need to provide authentication credentials.");
        }

        try {
            return testService.getTestsPaginate(pageNumber, perPage, utils.getToken(req));
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
