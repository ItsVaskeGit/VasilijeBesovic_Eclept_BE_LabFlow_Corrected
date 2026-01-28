package me.vasilije.labflow.api;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import me.vasilije.labflow.dto.request.HospitalCreateDTO;
import me.vasilije.labflow.dto.response.HospitalDTO;
import me.vasilije.labflow.service.HospitalService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HospitalAPI {

    private final HospitalService hospitalService;

    @RolesAllowed("admin")
    @RequestMapping(path="/hospital", method = RequestMethod.POST)
    public HospitalDTO createHospital(@RequestBody HospitalCreateDTO hospital) {
        return hospitalService.createHospital(hospital);
    }
}
