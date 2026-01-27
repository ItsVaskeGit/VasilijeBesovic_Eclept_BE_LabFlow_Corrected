package me.vasilije.labflow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HospitalDTO {

    @NotBlank(message = "Hospital needs to have a name.")
    private String name;
}
