package me.vasilije.labflow.api;

import jakarta.servlet.http.HttpServletRequest;
import me.vasilije.labflow.dto.HospitalDTO;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.repository.UserRepository;
import me.vasilije.labflow.service.HospitalService;
import me.vasilije.labflow.utils.TokenUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HospitalAPI {

    private final HospitalService hospitalService;
    private final TokenUtils utils;

    public HospitalAPI(HospitalService hospitalService, TokenUtils utils) {
        this.hospitalService = hospitalService;
        this.utils = utils;
    }

    @RequestMapping(path="/hospital", method = RequestMethod.POST)
    public ResponseEntity createHospital(@RequestBody HospitalDTO hospital, HttpServletRequest req) {


        if(!utils.requestHasToken(req)) {
            return ResponseEntity.status(401).body("You need to provide authentication credentials.");
        }

        if(!utils.stillValid(utils.getToken(req))) {
            return ResponseEntity.status(401).body("Your session has expired");
        }

        try {
            return hospitalService.createHospital(hospital, utils.getToken(req));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
