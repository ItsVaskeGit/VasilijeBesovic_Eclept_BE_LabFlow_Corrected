package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import me.vasilije.labflow.dto.LoginDTO;
import me.vasilije.labflow.dto.RegisterDTO;
import me.vasilije.labflow.exception.TypeNotFoundException;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.model.Technician;
import me.vasilije.labflow.model.User;
import me.vasilije.labflow.repository.HospitalRepository;
import me.vasilije.labflow.repository.TechnicianRepository;
import me.vasilije.labflow.repository.UserRepository;
import me.vasilije.labflow.utils.TokenUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final TechnicianRepository technicianRepository;

    private final HospitalRepository hospitalRepository;

    private final TokenUtils utils;

    public UserService(UserRepository userRepository, TechnicianRepository technicianRepository, HospitalRepository hospitalRepository, TokenUtils utils) {
        this.userRepository = userRepository;
        this.technicianRepository = technicianRepository;
        this.hospitalRepository = hospitalRepository;
        this.utils = utils;
    }

    @Transactional
    public ResponseEntity registerNewUser(RegisterDTO register, boolean isTechnician) {

        if(userRepository.existsByUsername(register.getUsername())) {
            return ResponseEntity.status(400).body("User with that username already exists.");
        }

        var newUser = new User();

        newUser.setUsername(register.getUsername());
        newUser.setPassword(BCrypt.hashpw(register.getPassword(), BCrypt.gensalt()));
        newUser.setTechnician(isTechnician);
        newUser.setAdmin(false);

        var savedUser = userRepository.save(newUser);

        var hospital = hospitalRepository.findById(register.getHospitalId()).orElseThrow(() -> new TypeNotFoundException("Hospital was not found."));

        if(isTechnician) {

            var newTechnician = new Technician();
            newTechnician.setBusy(false);
            newTechnician.setUser(savedUser);
            newTechnician.setHospital(hospital);

            technicianRepository.save(newTechnician);
        }

        return ResponseEntity.status(200).body(savedUser);
    }

    public ResponseEntity login(LoginDTO login) throws ResponseStatusException {

        if(!userRepository.existsByUsername(login.getUsername())) {
            return ResponseEntity.status(401).body("Invalid username or password.");
        }

        var user = userRepository.findByUsername(login.getUsername()).get();

        if(!BCrypt.checkpw(login.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid username or password.");
        }

        return ResponseEntity.status(200).body(utils.fetchToken(login.getUsername()));
    }

    @Transactional
    public ResponseEntity promote(String username, String jwtToken) throws UserNotFoundException {

        var currentUser = userRepository.findByUsername(utils.getUsername(jwtToken)).get();

        if(!currentUser.isAdmin()) {
            return ResponseEntity.status(401).body("Current user is not authenticated.");
        }

        if(!userRepository.existsByUsername(username)) {
            return ResponseEntity.status(404).body("User not found.");
        }

        var user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setAdmin(true);

        return ResponseEntity.status(200).build();
    }
}
