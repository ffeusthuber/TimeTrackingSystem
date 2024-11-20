package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ChangePasswordUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class ChangePasswordServiceTest {

    @Test
    void canChangePasswordWhenCorrectOldPasswordIsGiven() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        long employeeId = 1L;
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        Employee employee = new Employee(employeeId, "Jane", "Doe", "j.doe@mail.com", passwordEncoder.encode(oldPassword), EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());
        EmployeeService employeeService = new EmployeeService(EmployeeRepositoryStub.withEmployee(employee));
        ChangePasswordUseCase changePasswordService = new ChangePasswordService(employeeService);

        changePasswordService.changePasswordForEmployee(employeeId, oldPassword, newPassword);

        String updatedPassword = employeeService.getEmployeeByID(employeeId).getPassword();
        assertThat(passwordEncoder.matches(newPassword, updatedPassword)).isTrue();
    }

    @Test
    void cannotChangePasswordWhenIncorrectOldPasswordIsGiven() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        long employeeId = 1L;
        String oldPassword = "oldPassword";
        String wrongOldPassword = "wrongOldPassword";
        String newPassword = "newPassword";
        Employee employee = new Employee(employeeId, "Jane", "Doe", "j.doe@mail.com", passwordEncoder.encode(oldPassword), EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());
        EmployeeService employeeService = new EmployeeService(EmployeeRepositoryStub.withEmployee(employee));
        ChangePasswordUseCase changePasswordService = new ChangePasswordService(employeeService);

        changePasswordService.changePasswordForEmployee(employeeId, wrongOldPassword, newPassword);

        String password = employeeService.getEmployeeByID(employeeId).getPassword();
        assertThat(passwordEncoder.matches(oldPassword, password)).isTrue();
    }
}
