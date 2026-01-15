package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.model.Technician;
import me.vasilije.labflow.model.User;
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

    private final TokenUtils utils;

    public UserService(UserRepository userRepository, TechnicianRepository technicianRepository, TokenUtils utils) {
        this.userRepository = userRepository;
        this.technicianRepository = technicianRepository;
        this.utils = utils;
    }

    @Transactional
    public ResponseEntity registerNewUser(String username, String unhashedPassword, boolean isTechnician) {

        if(checkUserExists(username)) {
            return ResponseEntity.status(400).body("User with that username already exists.");
        }

        var newUser = new User();

        newUser.setUsername(username);
        newUser.setPassword(BCrypt.hashpw(unhashedPassword, BCrypt.gensalt()));
        newUser.setTechnician(isTechnician);
        newUser.setAdmin(false);

        var savedUser = userRepository.save(newUser);

        if(isTechnician) {

            var newTechnician = new Technician();
            newTechnician.setBusy(false);
            newTechnician.setUser(savedUser);

            technicianRepository.save(newTechnician);
        }

        return ResponseEntity.status(200).body(savedUser);
    }

    public ResponseEntity login(String username, String password) throws ResponseStatusException {

        if(!checkUserExists(username)) {
            return ResponseEntity.status(401).body("Invalid username or password.");
        }

        var user = userRepository.findByUsername(username).get();

        if(!BCrypt.checkpw(password, user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid username or password.");
        }

        return ResponseEntity.status(200).body(utils.fetchToken(username));
    }

    @Transactional
    public ResponseEntity promote(String username, String jwtToken) throws UserNotFoundException {

        var currentUser = userRepository.findByUsername(utils.getUsername(jwtToken)).get();

        if(!currentUser.isAdmin()) {
            return ResponseEntity.status(401).body("Current user is not authenticated.");
        }

        if(!checkUserExists(username)) {
            return ResponseEntity.status(404).body("User not found.");
        }

        var user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setAdmin(true);

        return ResponseEntity.status(200).build();
    }

    public boolean checkUserExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
