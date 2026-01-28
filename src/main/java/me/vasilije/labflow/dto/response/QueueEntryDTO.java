package me.vasilije.labflow.dto.response;

import me.vasilije.labflow.model.TestType;

public record QueueEntryDTO(long id, TestType testType) {
}
