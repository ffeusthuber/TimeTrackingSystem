package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.EmployeeDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin.EmployeeManagementUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeManagementServiceTest {

    @Test
    void canCreateEmployee() {
        //create Employee endpoint can only be reached with role ADMIN
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withoutEmployees();
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        EmployeeManagementUseCase employeeManagementService = new EmployeeManagementService(employeeService);

        employeeManagementService.createEmployee("Jane", "Doe", "j.doe@test-mail.com", "password", "USER");

        assertThat(employeeRepository.getEmployees()).isNotNull();
    }


    @Test
    void canGetEmployeeById() {
        Employee employee = new Employee(1L, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER);
        EmployeeRepository employeeRepositoryStub = EmployeeRepositoryStub.withEmployee(employee);
        EmployeeManagementUseCase employeeManagementService = new EmployeeManagementService(new EmployeeService(employeeRepositoryStub));

        EmployeeDTO employeeDTO = employeeManagementService.getEmployee(1L);

        assertThat(employeeDTO.getFullName()).isEqualTo("Jane Doe");
        assertThat(employeeDTO.getRole()).isEqualTo("USER");
    }
}
