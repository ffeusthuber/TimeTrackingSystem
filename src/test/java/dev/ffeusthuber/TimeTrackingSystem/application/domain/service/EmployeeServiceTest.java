package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepositoryStub;
import org.junit.jupiter.api.Test;

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
}
