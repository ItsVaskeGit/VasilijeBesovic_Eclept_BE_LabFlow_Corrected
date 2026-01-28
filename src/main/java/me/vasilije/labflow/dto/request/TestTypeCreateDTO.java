package me.vasilije.labflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TestTypeCreateDTO {

    @NotBlank(message = "Test type needs to have a name.")
    private String name;

    @NotNull(message = "Test type needs to have an excecution duration.")
    private int duration;

    @NotNull(message = "Test type needs to use a certain number of reagents to be completed.")
    private int reagentsNeeded;
}
