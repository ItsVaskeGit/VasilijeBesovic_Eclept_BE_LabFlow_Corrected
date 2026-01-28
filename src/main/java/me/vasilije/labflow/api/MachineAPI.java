package me.vasilije.labflow.api;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import me.vasilije.labflow.dto.response.MachineDTO;
import me.vasilije.labflow.service.MachineService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MachineAPI {

    private final MachineService machineService;

    @RolesAllowed("admin")
    @RequestMapping(path = "/machine", method = RequestMethod.POST)
    public MachineDTO createMachine() {
        return machineService.createMachine();
    }

    @RolesAllowed("admin")
    @RequestMapping(path = "/assign/{userId}/{machineId}", method = RequestMethod.PUT)
    public MachineDTO assignMachine(@PathVariable long userId, @PathVariable long machineId) {
        return machineService.assignMachine(machineId, userId);
    }
}
