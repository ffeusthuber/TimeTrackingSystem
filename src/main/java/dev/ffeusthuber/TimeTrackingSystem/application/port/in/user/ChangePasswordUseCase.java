package dev.ffeusthuber.TimeTrackingSystem.application.port.in.user;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.ChangePasswordResponse;

public interface ChangePasswordUseCase {
    ChangePasswordResponse changePasswordForEmployee(long employeeId, String oldPassword, String newPassword);
}
