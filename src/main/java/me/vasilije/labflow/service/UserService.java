package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.vasilije.labflow.dto.request.RegisterDTO;
import me.vasilije.labflow.exception.TypeNotFoundException;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.model.Technician;
import me.vasilije.labflow.model.User;
import me.vasilije.labflow.repository.HospitalRepository;
import me.vasilije.labflow.repository.TechnicianRepository;
import me.vasilije.labflow.repository.UserRepository;
import org.springframework.security.crypto.password4j.BcryptPassword4jPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final TechnicianRepository technicianRepository;

    private final HospitalRepository hospitalRepository;

    @Transactional
    public boolean registerNewUser(RegisterDTO register) {

        if(userRepository.existsByUsername(register.getUsername())) {
            return false;
        }

        var newUser = new User();

        newUser.setUsername(register.getUsername());
        newUser.setPassword(new BcryptPassword4jPasswordEncoder().encode(register.getPassword()));
        newUser.setTechnician(register.isTechnician());
        newUser.setRoles("user");

        var savedUser = userRepository.save(newUser);

        if(register.isTechnician()) {
            var hospital = hospitalRepository.findById(register.getHospitalId()).orElseThrow(() -> new TypeNotFoundException("Hospital was not found."));
            var newTechnician = new Technician();
            newTechnician.setBusy(false);
            newTechnician.setUser(savedUser);
            newTechnician.setHospital(hospital);

            technicianRepository.save(newTechnician);
        }

        return true;
    }


    @Transactional
    public boolean promote(String username) throws UserNotFoundException {

        var user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setRoles("admin");

        return true;
    }
}
