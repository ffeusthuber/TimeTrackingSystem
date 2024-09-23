package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeServiceTest {

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
        EmployeeService employeeService = new EmployeeService(employeeRepository);

        assertThat(employeeService.getClockStateForEmployee(clockedInEmployeeId)).isEqualTo(ClockState.CLOCKED_IN);
        assertThat(employeeService.getClockStateForEmployee(clockedOutEmployeeId)).isEqualTo(ClockState.CLOCKED_OUT);
        assertThat(employeeService.getClockStateForEmployee(pausedEmployeeId)).isEqualTo(ClockState.ON_PAUSE);
    }

    @Test
    void canSetClockStateForEmployee() {
        long employeeId = 1L;
        Employee employee = new Employee(employeeId, ClockState.CLOCKED_OUT);
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withEmployee(employee);
        EmployeeService employeeService = new EmployeeService(employeeRepository);

        employeeService.setClockStateForEmployee(employeeId, ClockState.CLOCKED_IN);

        assertThat(employee.getClockState()).isEqualTo(ClockState.CLOCKED_IN);
    }

    @Test
    void createdEmployeeGetsAssignedId() {
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withoutEmployees();
        EmployeeService employeeService = new EmployeeService(employeeRepository);

        Employee createdEmployee = employeeService.createEmployee("Jane", "Doe", "j.doe@test-mail.com", "password", "USER");

        assertThat(createdEmployee.getEmployeeID()).isNotNull();
    }

    @Test
    void createdEmployeePasswordIsEncrypted() throws NoSuchFieldException, IllegalAccessException {
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withoutEmployees();
        EmployeeService employeeService = new EmployeeService(employeeRepository);

        String rawPassword = "password";
        Employee createdEmployee = employeeService.createEmployee("Jane", "Doe", "j.doe@test-mail.com", rawPassword, "USER");

        // Use reflection to access the private password field to avoid password exposure
        Field passwordField = Employee.class.getDeclaredField("password");
        passwordField.setAccessible(true);
        String encryptedPassword = (String) passwordField.get(createdEmployee);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertThat(passwordEncoder.matches(rawPassword, encryptedPassword)).isTrue();
    }
}
