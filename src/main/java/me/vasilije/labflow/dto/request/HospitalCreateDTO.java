package me.vasilije.labflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HospitalCreateDTO {

    @NotBlank(message = "Hospital needs to have a name.")
    private String name;
}
