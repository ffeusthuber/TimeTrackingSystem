package dev.ffeusthuber.TimeTrackingSystem.application.port.in.setup;

public interface CreateInitialAdminUseCase {
    void createInitialAdmin(String firstName, String lastName, String email, String password);
}
