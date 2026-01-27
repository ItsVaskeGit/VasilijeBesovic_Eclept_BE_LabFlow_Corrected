package me.vasilije.labflow.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotBlank(message = "Username is required to register.")
    private String username;

    @NotBlank(message = "Password is required to register.")
    @Size(min = 8, message = "Password needs to be 8 characters long.")
    private String password;

    @JsonProperty
    @NotNull(message = "You need to specify whether a user is a technician or not.")
    private boolean isTechnician;

    @NotNull(message = "You need to specify a number here, if user is not a technician then put 0.")
    private long hospitalId;

}