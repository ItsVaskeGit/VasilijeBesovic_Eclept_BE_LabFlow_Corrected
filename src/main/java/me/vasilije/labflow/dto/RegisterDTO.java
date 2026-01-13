package me.vasilije.labflow.dto;

import lombok.Data;

@Data
public class RegisterDTO {

    private String username;
    private String password;
    private boolean isTechnician;

}