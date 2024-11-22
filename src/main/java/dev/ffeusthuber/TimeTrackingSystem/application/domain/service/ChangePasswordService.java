package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ChangePasswordUseCase;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordService implements ChangePasswordUseCase {
    private final EmployeeService employeeService;


    public ChangePasswordService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ChangePasswordResponse changePasswordForEmployee(long employeeId, String oldPassword, String newPassword) {
        if(employeeService.isCorrectPasswordForEmployee(employeeId, oldPassword)) {
            employeeService.setPasswordForEmployee(employeeId, newPassword);
            return new ChangePasswordResponse(ChangePasswordResponseStatus.SUCCESS);
        } else {
            return new ChangePasswordResponse(ChangePasswordResponseStatus.WRONG_PASSWORD);
        }
    }

}
