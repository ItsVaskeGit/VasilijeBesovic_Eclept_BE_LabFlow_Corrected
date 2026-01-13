package me.vasilije.labflow.service;

import me.vasilije.labflow.model.Technician;
import me.vasilije.labflow.model.User;
import me.vasilije.labflow.repository.TechnicianRepository;
import me.vasilije.labflow.repository.UserRepository;
import me.vasilije.labflow.utils.TokenUtils;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final TechnicianRepository technicianRepository;

    private final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final TokenUtils utils = new TokenUtils();

    public UserService(UserRepository userRepository, TechnicianRepository technicianRepository) {
        this.userRepository = userRepository;
        this.technicianRepository = technicianRepository;
    }

    public boolean registerNewUser(String username, String unhashedPassword, boolean isTechnician) {

        if(checkUserExists(username)) {
            return false;
        }

        var newUser = new User();

        newUser.setUsername(username);
        newUser.setPassword(encoder.encode(unhashedPassword));
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

    public String login(String username, String password) {

        if(!checkUserExists(username)) {
            return null;
        }

        var user = userRepository.findByUsername(username);

        if(!encoder.matches(password, user.getPassword())) {
            return null;
        }

        return utils.fetchToken(username);
    }

    public void promote(String username) {

    }

    public boolean checkUserExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
