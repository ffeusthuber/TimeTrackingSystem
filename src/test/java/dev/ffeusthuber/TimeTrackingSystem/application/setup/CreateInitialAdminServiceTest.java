package dev.ffeusthuber.TimeTrackingSystem.application.setup;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.setup.CreateInitialAdminUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import dev.ffeusthuber.TimeTrackingSystem.application.service.CreateInitialAdminService;
import dev.ffeusthuber.TimeTrackingSystem.application.service.EmployeeService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateInitialAdminServiceTest {

    @Test
    void whenNoEmployeesExistInitialAdminIsCreated() {
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withoutEmployees();
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        CreateInitialAdminUseCase createInitialAdminService = new CreateInitialAdminService(employeeService);

        createInitialAdminService.createInitialAdmin("firstName", "lastName", "f.lastName@mail.com", "password");

        assertThat(employeeService.getAllEmployees()).hasSize(1);
    }

    @Test
    void whenEmployeesExistInitialAdminIsNotCreated() {
        Employee employee = new Employee(1L, ClockState.CLOCKED_OUT, WorkSchedule.createDefaultWorkSchedule());
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withEmployee(employee);
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        CreateInitialAdminUseCase createInitialAdminService = new CreateInitialAdminService(employeeService);

        createInitialAdminService.createInitialAdmin("firstName", "lastName", "f.lastName@mail.com", "password");

        assertThat(employeeService.getAllEmployees()).hasSize(1);
    }
}
