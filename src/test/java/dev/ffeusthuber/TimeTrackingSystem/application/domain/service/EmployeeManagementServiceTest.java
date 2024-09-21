package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin.EmployeeManagementUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepositoryStub;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeManagementServiceTest {

    @Test
    void adminCanCreateEmployee() {
        //create Employee endpoint can only be reached with role ADMIN
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withoutEmployees();
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        EmployeeManagementUseCase employeeManagementService = new EmployeeManagementService(employeeService);

        Employee employee = employeeManagementService.createEmployee(1L,"Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER);

        assertThat(employeeRepository.getEmployees()).isEqualTo(List.of(employee));
    }
}
