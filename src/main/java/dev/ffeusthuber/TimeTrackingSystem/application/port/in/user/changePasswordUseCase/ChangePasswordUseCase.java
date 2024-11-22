package dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.changePasswordUseCase;

public interface ChangePasswordUseCase {
    ChangePasswordResponse changePasswordForEmployee(long employeeId, String oldPassword, String newPassword);
}
