package me.vasilije.labflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterDTO {

    private String username;
    private String password;
    @JsonProperty
    private boolean isTechnician;

}