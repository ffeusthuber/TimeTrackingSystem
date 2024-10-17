package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import dev.ffeusthuber.TimeTrackingSystem.config.WorkScheduleConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeServiceTest {

    private static WorkScheduleConfig workScheduleConfig;


    @BeforeAll
    static void setUp() {
        workScheduleConfig = new WorkScheduleConfig();
    }

    @Test
    void createdEmployeeGetsAssignedId() {
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withoutEmployees();
        EmployeeService employeeService = createEmployeeService(employeeRepository);

        Employee createdEmployee = employeeService.createEmployee("Jane", "Doe", "j.doe@test-mail.com", "password", "USER");

        assertThat(createdEmployee.getEmployeeID()).isNotNull();
    }

    @Test
    void createdEmployeeGetsAssignedTheDefaultWorkSchedule() {
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withoutEmployees();
        EmployeeService employeeService = createEmployeeService(employeeRepository);
        WorkScheduleConfig workScheduleConfig = new WorkScheduleConfig();

        Employee createdEmployee = employeeService.createEmployee("Jane", "Doe", "j.doe@test-mail.com", "password", "USER");

        assertThat(createdEmployee.getWorkSchedule()).isEqualTo(workScheduleConfig.defaultWorkSchedule());
    }

    @Test
    void canGetClockStateForEmployees() {
        long clockedInEmployeeId = 1L;
        long clockedOutEmployeeId = 2L;
        long pausedEmployeeId = 3L;
        Employee clockedInEmployee = new Employee(clockedInEmployeeId, ClockState.CLOCKED_IN);
        Employee clockedOutEmployee = new Employee(clockedOutEmployeeId, ClockState.CLOCKED_OUT);
        Employee pausedEmployee = new Employee(pausedEmployeeId, ClockState.ON_PAUSE);
        List<Employee> employees = List.of(clockedInEmployee, clockedOutEmployee, pausedEmployee);
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withEmployees(employees);
        EmployeeService employeeService = createEmployeeService(employeeRepository);

        assertThat(employeeService.getClockStateForEmployee(clockedInEmployeeId)).isEqualTo(ClockState.CLOCKED_IN);
        assertThat(employeeService.getClockStateForEmployee(clockedOutEmployeeId)).isEqualTo(ClockState.CLOCKED_OUT);
        assertThat(employeeService.getClockStateForEmployee(pausedEmployeeId)).isEqualTo(ClockState.ON_PAUSE);
    }

    @Test
    void canSetClockStateForEmployee() {
        long employeeId = 1L;
        Employee employee = new Employee(employeeId, ClockState.CLOCKED_OUT);
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withEmployee(employee);
        EmployeeService employeeService = createEmployeeService(employeeRepository);

        employeeService.setClockStateForEmployee(employeeId, ClockState.CLOCKED_IN);

        assertThat(employeeRepository.getEmployeeByID(employeeId).getClockState()).isEqualTo(ClockState.CLOCKED_IN);
    }



    @Test
    void createdEmployeePasswordIsEncrypted() {
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withoutEmployees();
        EmployeeService employeeService = createEmployeeService(employeeRepository);

        String rawPassword = "password";
        Employee createdEmployee = employeeService.createEmployee("Jane", "Doe", "j.doe@test-mail.com", rawPassword, "USER");

        String encryptedPassword = createdEmployee.getPassword();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertThat(passwordEncoder.matches(rawPassword, encryptedPassword)).isTrue();
    }

    @Test
    void canGetEmployeeById() {
        Employee employee = new Employee(1L, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, workScheduleConfig.defaultWorkSchedule());
        EmployeeRepository employeeRepositoryStub = EmployeeRepositoryStub.withEmployee(employee);
        EmployeeService employeeService = createEmployeeService(employeeRepositoryStub);

        Employee foundEmployee = employeeService.getEmployeeById(1L);

        assertThat(foundEmployee).isEqualTo(employee);
    }

    private static EmployeeService createEmployeeService(EmployeeRepository employeeRepository) {
        return new EmployeeService(employeeRepository, workScheduleConfig);
    }
}
