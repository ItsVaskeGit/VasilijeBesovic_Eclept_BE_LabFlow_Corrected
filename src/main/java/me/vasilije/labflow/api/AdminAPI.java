package me.vasilije.labflow.api;

import jakarta.servlet.http.HttpServletRequest;
import me.vasilije.labflow.dto.TestTypeDTO;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.service.TestService;
import me.vasilije.labflow.service.TestTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminAPI {

    private final TestTypeService testTypeService;
    private final TestService testService;

    public AdminAPI(TestTypeService testTypeService, TestService testService) {
        this.testTypeService = testTypeService;
        this.testService = testService;

    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody TestTypeDTO newTestType, HttpServletRequest req) {

        var jwtToken = req.getHeader("Authorization").split(" ")[1];

        try {
            return testTypeService.createNewTestType(newTestType, jwtToken);
        }catch(UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @RequestMapping(path = "/modify", method = RequestMethod.PUT)
    public ResponseEntity modify(@RequestBody TestTypeDTO modifiedTestType, HttpServletRequest req) {

        var jwtToken = req.getHeader("Authorization").split(" ")[1];

        try {
            return testTypeService.modifyTestType(modifiedTestType, jwtToken);
        }catch(UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable long id, HttpServletRequest req) {

        var jwtToken = req.getHeader("Authorization").split(" ")[1];

        try {
            return testTypeService.deleteTestType(id, jwtToken);
        }catch(UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @RequestMapping(path = "/tests/{pageNumber}/{perPage}", method = RequestMethod.GET)
    public ResponseEntity getTests(@PathVariable int pageNumber, @PathVariable int perPage, HttpServletRequest req) {

        var jwtToken = req.getHeader("Authorization").split(" ")[1];

        try {
            return testService.getTestsPaginate(pageNumber, perPage, jwtToken);
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
