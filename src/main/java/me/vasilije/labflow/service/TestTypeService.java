package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.vasilije.labflow.dto.request.TestTypeCreateDTO;
import me.vasilije.labflow.dto.request.TestTypeModifyDTO;
import me.vasilije.labflow.dto.response.TestTypeDTO;
import me.vasilije.labflow.exception.TypeNotFoundException;
import me.vasilije.labflow.model.TestType;
import me.vasilije.labflow.repository.TestTypeRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestTypeService {

    private final TestTypeRepository testTypeRepository;

    @Transactional
    public TestTypeDTO createNewTestType(TestTypeCreateDTO newTest) {

        var newTestType = new TestType();

        newTestType.setDuration(newTest.getDuration());
        newTestType.setName(newTest.getName());
        newTestType.setReagentUnitsNeeded(newTest.getReagentsNeeded());

        var savedTestType = testTypeRepository.save(newTestType);

        return new TestTypeDTO(savedTestType.getName(), savedTestType.getDuration(), savedTestType.getReagentUnitsNeeded());
    }

    @Transactional
    public TestTypeDTO modifyTestType(TestTypeModifyDTO newTest) {

        var type = testTypeRepository.findById(newTest.getId()).orElseThrow(() -> new TypeNotFoundException("Test type with given information was not found."));

        type.setDuration(newTest.getDuration());
        type.setName(newTest.getName());
        type.setReagentUnitsNeeded(newTest.getReagentsNeeded());

        return new TestTypeDTO(type.getName(), type.getDuration(), type.getReagentUnitsNeeded());
    }

    @Transactional
    public Boolean deleteTestType(long id) {
        var type = testTypeRepository.findById(id).orElseThrow(() -> new TypeNotFoundException("Test type with given information was not found."));

        testTypeRepository.delete(type);

        return true;
    }
}
