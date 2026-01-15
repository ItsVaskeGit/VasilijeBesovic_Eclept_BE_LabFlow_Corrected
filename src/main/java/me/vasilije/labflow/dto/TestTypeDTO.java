package me.vasilije.labflow.dto;

import lombok.Data;

@Data
public class TestTypeDTO {

    private long id;
    private String name;
    private int duration;
    private int reagentsNeeded;
}
