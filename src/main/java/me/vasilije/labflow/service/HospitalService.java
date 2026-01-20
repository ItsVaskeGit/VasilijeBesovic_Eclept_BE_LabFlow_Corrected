package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import me.vasilije.labflow.dto.HospitalDTO;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.model.Hospital;
import me.vasilije.labflow.model.Queue;
import me.vasilije.labflow.repository.HospitalRepository;
import me.vasilije.labflow.repository.QueueRepository;
import me.vasilije.labflow.repository.UserRepository;
import me.vasilije.labflow.utils.TokenUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final UserRepository userRepository;
    private final QueueRepository queueRepository;
    private final TokenUtils utils;

    public HospitalService(HospitalRepository hospitalRepository, UserRepository userRepository, QueueRepository queueRepository, TokenUtils utils) {
        this.hospitalRepository = hospitalRepository;
        this.userRepository = userRepository;
        this.queueRepository = queueRepository;
        this.utils = utils;
    }

    @Transactional
    public ResponseEntity createHospital(HospitalDTO hospital, String jwtToken) {

        var user = userRepository.findByUsername(utils.getUsername(jwtToken)).orElseThrow(() -> new UserNotFoundException("User was not found."));

        if(!user.isAdmin()) {
            return ResponseEntity.status(401).body("You are not authorized to do this action.");
        }

        var newHospital = new Hospital();

        newHospital.setName(hospital.getName());

        var savedHospital = hospitalRepository.save(newHospital);

        var queue = new Queue();

        queue.setHospital(savedHospital);

        queueRepository.save(queue);

        return ResponseEntity.status(200).body(savedHospital);
    }
}
