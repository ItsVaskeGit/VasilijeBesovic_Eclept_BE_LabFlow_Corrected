package me.vasilije.labflow.dto.response;

import me.vasilije.labflow.model.SubmitType;
import me.vasilije.labflow.model.TestType;

public record TestDTO(long id, SubmitType submitType, TestType testType, boolean finished) { }
