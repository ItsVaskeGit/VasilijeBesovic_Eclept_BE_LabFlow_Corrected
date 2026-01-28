package me.vasilije.labflow.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.vasilije.labflow.dto.request.HospitalCreateDTO;
import me.vasilije.labflow.dto.response.HospitalDTO;
import me.vasilije.labflow.model.Hospital;
import me.vasilije.labflow.model.Queue;
import me.vasilije.labflow.repository.HospitalRepository;
import me.vasilije.labflow.repository.QueueRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final QueueRepository queueRepository;

    @Transactional
    public HospitalDTO createHospital(HospitalCreateDTO hospital) {

        var newHospital = new Hospital();

        newHospital.setName(hospital.getName());

        var savedHospital = hospitalRepository.save(newHospital);

        var queue = new Queue();

        queue.setHospital(savedHospital);

        queueRepository.save(queue);

        return new HospitalDTO(savedHospital.getId(), savedHospital.getName());
    }
}
