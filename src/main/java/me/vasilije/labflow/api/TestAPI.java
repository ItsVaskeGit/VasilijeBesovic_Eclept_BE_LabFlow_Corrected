package me.vasilije.labflow.api;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.vasilije.labflow.dto.response.QueueEntryDTO;
import me.vasilije.labflow.dto.response.TestDTO;
import me.vasilije.labflow.service.TestService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestAPI {

    private final TestService testService;

    @RolesAllowed("user")
    @RequestMapping(path = "/check", method = RequestMethod.GET)
    public List<TestDTO> checkPatientTests(HttpServletRequest req) {
        return testService.checkPatientTests(req.getUserPrincipal().getName());
    }

    @RolesAllowed("user")
    @RequestMapping(path = "/check/{id}", method = RequestMethod.GET)
    public TestDTO checkTest(@PathVariable long id, HttpServletRequest req) {
        return testService.checkTest(id, req.getUserPrincipal().getName());
    }

    @RolesAllowed("user")
    @RequestMapping(path = "/schedule/{hospitalId}/{id}/{submitTypeId}", method = RequestMethod.POST)
    public QueueEntryDTO scheduleTest(@PathVariable long hospitalId , @PathVariable long id, @PathVariable long submitTypeId, HttpServletRequest req) {
        return testService.addTestToQueue(id, submitTypeId, hospitalId, req.getUserPrincipal().getName());
    }

}
