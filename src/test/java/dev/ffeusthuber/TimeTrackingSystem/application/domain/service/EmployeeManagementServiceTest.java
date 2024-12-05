package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.EmployeeDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WorkScheduleDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin.EmployeeManagementUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeManagementServiceTest {

    @Test
    void canCreateEmployee() {
        //create Employee endpoint can only be reached with role ADMIN
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withoutEmployees();
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        EmployeeManagementUseCase employeeManagementService = new EmployeeManagementService(employeeService);

        employeeManagementService.createEmployee("Jane", "Doe", "j.doe@test-mail.com", "password", "USER", new WorkScheduleDTO(8, 8, 8, 8, 8, 0, 0));

        assertThat(employeeRepository.getAllEmployees()).isNotNull();
    }


    @Test
    void canGetEmployeeDetailsById() {
        Employee employee = new Employee(1L, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());
        EmployeeRepository employeeRepositoryStub = EmployeeRepositoryStub.withEmployee(employee);
        EmployeeManagementUseCase employeeManagementService = new EmployeeManagementService(new EmployeeService(employeeRepositoryStub));

        EmployeeDTO employeeDTO = employeeManagementService.getEmployeeDetails(1L);

        assertThat(employeeDTO.fullName()).isEqualTo("Jane Doe");
        assertThat(employeeDTO.role()).isEqualTo("USER");
    }

    @Test
    void canGetDefaultWorkSchedule() {
        EmployeeService employeeService = new EmployeeService(EmployeeRepositoryStub.withoutEmployees());
        EmployeeManagementUseCase employeeManagementService = new EmployeeManagementService(employeeService);

        WorkScheduleDTO workScheduleDTO = employeeManagementService.getDefaultWorkSchedule();

        assertThat(workScheduleDTO).isEqualTo(new WorkScheduleDTO(WorkSchedule.createDefaultWorkSchedule()));
    }

    @Test
    void canGetEmployeeList(){
        Employee employee = new Employee(1L, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());
        EmployeeRepository employeeRepositoryStub = EmployeeRepositoryStub.withEmployee(employee);
        EmployeeManagementUseCase employeeManagementService = new EmployeeManagementService(new EmployeeService(employeeRepositoryStub));
        List<EmployeeDTO> expectedList = List.of(new EmployeeDTO(employee));

        List<EmployeeDTO> actualList = employeeManagementService.getEmployeeList();

        assertThat(actualList).isEqualTo(expectedList);
    }
}
