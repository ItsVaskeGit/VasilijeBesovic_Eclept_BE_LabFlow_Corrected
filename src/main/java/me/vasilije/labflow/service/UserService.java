package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import me.vasilije.labflow.model.Technician;
import me.vasilije.labflow.model.User;
import me.vasilije.labflow.repository.TechnicianRepository;
import me.vasilije.labflow.repository.UserRepository;
import me.vasilije.labflow.utils.TokenUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final TechnicianRepository technicianRepository;

    private final TokenUtils utils = new TokenUtils();

    public UserService(UserRepository userRepository, TechnicianRepository technicianRepository) {
        this.userRepository = userRepository;
        this.technicianRepository = technicianRepository;
    }

    @Transactional
    public boolean registerNewUser(String username, String unhashedPassword, boolean isTechnician) {

        if(checkUserExists(username)) {
            return false;
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

        return true;
    }

    public String login(String username, String password) throws ResponseStatusException {

        if(!checkUserExists(username)) {
            return null;
        }

        var user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(!BCrypt.checkpw(password, user.getPassword())) {
            return null;
        }

        return utils.fetchToken(username);
    }

    @Transactional
    public boolean promote(String username) throws ResponseStatusException {

        if(!checkUserExists(username)) {
            return false;
        }

        var user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        user.setAdmin(true);

        return true;
    }

    public boolean checkUserExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
