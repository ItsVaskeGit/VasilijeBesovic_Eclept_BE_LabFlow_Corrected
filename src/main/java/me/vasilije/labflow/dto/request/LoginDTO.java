package me.vasilije.labflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank(message = "Username is required to login.")
    private String username;

    @NotBlank(message = "Password is required to login.")
    private String password;

}
