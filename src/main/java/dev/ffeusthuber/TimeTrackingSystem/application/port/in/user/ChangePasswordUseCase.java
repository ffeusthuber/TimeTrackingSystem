package dev.ffeusthuber.TimeTrackingSystem.application.port.in.user;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.WrongPasswordException;

public interface ChangePasswordUseCase {
    void changePasswordForEmployee(long employeeId, String oldPassword, String newPassword) throws WrongPasswordException;
}
