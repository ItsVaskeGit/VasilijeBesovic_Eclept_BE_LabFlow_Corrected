package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import me.vasilije.labflow.dto.TestTypeDTO;
import me.vasilije.labflow.exception.TypeNotFoundException;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.model.TestType;
import me.vasilije.labflow.repository.TestTypeRepository;
import me.vasilije.labflow.repository.UserRepository;
import me.vasilije.labflow.utils.TokenUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TestTypeService {

    private final TestTypeRepository testTypeRepository;
    private final UserRepository userRepository;
    private final TokenUtils utils;

    public TestTypeService(TestTypeRepository testTypeRepository, UserRepository userRepository, TokenUtils utils) {
        this.testTypeRepository = testTypeRepository;
        this.userRepository = userRepository;
        this.utils = utils;
    }

    @Transactional
    public ResponseEntity createNewTestType(TestTypeDTO newTest, String jwtToken) {

        if(!utils.stillValid(jwtToken)) {
            return ResponseEntity.status(401).body("Your token is expired or invalid.");
        }

        var user = userRepository.findByUsername(utils.getUsername(jwtToken)).orElseThrow(() -> new UserNotFoundException("User not found."));

        if(!user.isAdmin()) {
            return ResponseEntity.status(401).body("You are not authorized to do this action.");
        }

        var newTestType = new TestType();

        newTestType.setDuration(newTest.getDuration());
        newTestType.setName(newTest.getName());
        newTestType.setReagentUnitsNeeded(newTest.getReagentsNeeded());

        return ResponseEntity.status(200).body(testTypeRepository.save(newTestType));
    }

    @Transactional
    public ResponseEntity modifyTestType(TestTypeDTO newTest, String jwtToken) {

        if(!utils.stillValid(jwtToken)) {
            return ResponseEntity.status(401).body("Your token is expired or invalid.");
        }

        var user = userRepository.findByUsername(utils.getUsername(jwtToken)).orElseThrow(() -> new UserNotFoundException("User not found."));

        if(!user.isAdmin()) {
            return ResponseEntity.status(401).body("You are not authorized to do this action.");
        }

        var type = testTypeRepository.findById(newTest.getId()).orElseThrow(() -> new TypeNotFoundException("Test type with given information was not found."));

        type.setDuration(newTest.getDuration());
        type.setName(newTest.getName());
        type.setReagentUnitsNeeded(newTest.getReagentsNeeded());

        return ResponseEntity.status(200).body(type);
    }

    @Transactional
    public ResponseEntity deleteTestType(long id, String jwtToken) {

        if(!utils.stillValid(jwtToken)) {
            return ResponseEntity.status(401).body("Your token is expired or invalid.");
        }

        var user = userRepository.findByUsername(utils.getUsername(jwtToken)).orElseThrow(() -> new UserNotFoundException("User not found."));

        if(!user.isAdmin()) {
            return ResponseEntity.status(401).body("You are not authorized to do this action.");
        }

        var type = testTypeRepository.findById(id).orElseThrow(() -> new TypeNotFoundException("Test type with given information was not found."));

        testTypeRepository.delete(type);

        return ResponseEntity.status(200).body("Success!");
    }
}
