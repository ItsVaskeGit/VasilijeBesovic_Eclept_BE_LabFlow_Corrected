package me.vasilije.labflow.exception;

public class NoMachinesAvailableException extends RuntimeException {
    public NoMachinesAvailableException(String message) {
        super(message);
    }
}
