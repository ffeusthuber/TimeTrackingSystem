package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Entered current password is incorrect");
    }
}
