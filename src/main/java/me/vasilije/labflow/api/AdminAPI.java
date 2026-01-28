package me.vasilije.labflow.api;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import me.vasilije.labflow.dto.request.TestTypeCreateDTO;
import me.vasilije.labflow.dto.request.TestTypeModifyDTO;
import me.vasilije.labflow.dto.response.TestDTO;
import me.vasilije.labflow.dto.response.TestTypeDTO;
import me.vasilije.labflow.service.TestService;
import me.vasilije.labflow.service.TestTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminAPI {

    private final TestTypeService testTypeService;
    private final TestService testService;

    @RolesAllowed("admin")
    @RequestMapping(path = "/test-type", method = RequestMethod.POST)
    public TestTypeDTO create(@RequestBody TestTypeCreateDTO newTestType) {
        return testTypeService.createNewTestType(newTestType);
    }

    @RolesAllowed("admin")
    @RequestMapping(path = "/test-type", method = RequestMethod.PUT)
    public TestTypeDTO modify(@RequestBody TestTypeModifyDTO modifiedTestType) {
        return testTypeService.modifyTestType(modifiedTestType);
    }

    @RolesAllowed("admin")
    @RequestMapping(path = "/test-type/{id}", method = RequestMethod.DELETE)
    public Boolean delete(@PathVariable long id) {
        return testTypeService.deleteTestType(id);
    }

    @RolesAllowed("admin")
    @RequestMapping(path = "/tests/{pageNumber}/{perPage}", method = RequestMethod.GET)
    public List<TestDTO> getTests(@PathVariable int pageNumber, @PathVariable int perPage) {
        return testService.getTestsPaginate(pageNumber, perPage);
    }
}
