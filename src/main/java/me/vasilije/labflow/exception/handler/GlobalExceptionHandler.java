package me.vasilije.labflow.exception.handler;

import me.vasilije.labflow.exception.NoMachinesAvailableException;
import me.vasilije.labflow.exception.TypeNotFoundException;
import me.vasilije.labflow.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public void handleUserNotFound() {}

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(NoMachinesAvailableException.class)
    public void handleNoMachinesAvailable() {}

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TypeNotFoundException.class)
    public void handleTypeNotFoundException() {}
}
