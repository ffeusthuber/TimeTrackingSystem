package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ChangePasswordUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class ChangePasswordServiceTest {

    private BCryptPasswordEncoder passwordEncoder;
    private long employeeId;
    private final String oldPassword = "oldPassword";
    private final String newPassword = "newPassword";
    private EmployeeService employeeService;
    private ChangePasswordUseCase changePasswordService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        employeeId = 1L;
        Employee employee = new Employee(employeeId,
                                         "Jane",
                                         "Doe",
                                         "j.doe@mail.com",
                                         passwordEncoder.encode(oldPassword),
                                         EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());
        employeeService = new EmployeeService(EmployeeRepositoryStub.withEmployee(employee));
        changePasswordService = new ChangePasswordService(employeeService);
    }

    @Test
    void canChangePasswordWhenCorrectOldPasswordIsGiven() {
        ChangePasswordResponse changePasswordResponse = changePasswordService.changePasswordForEmployee(employeeId, oldPassword, newPassword);

        String updatedPassword = employeeService.getEmployeeByID(employeeId).getPassword();
        assertThat(changePasswordResponse.status()).isEqualTo(ChangePasswordResponseStatus.SUCCESS);
        assertThat(passwordEncoder.matches(newPassword, updatedPassword)).isTrue();
    }

    @Test
    void cannotChangePasswordWhenIncorrectOldPasswordIsGiven() {
        ChangePasswordResponse changePasswordResponse = changePasswordService.changePasswordForEmployee(employeeId, "wrongOldPassword", newPassword);

        String password = employeeService.getEmployeeByID(employeeId).getPassword();
        assertThat(passwordEncoder.matches(oldPassword, password)).isTrue();
        assertThat(changePasswordResponse.status()).isEqualTo(ChangePasswordResponseStatus.WRONG_PASSWORD);
    }
}
