package dev.ffeusthuber.TimeTrackingSystem.application.port.in.user;

public interface ChangePasswordUseCase {
    void changePasswordForEmployee(long employeeId, String oldPassword, String newPassword);
}
