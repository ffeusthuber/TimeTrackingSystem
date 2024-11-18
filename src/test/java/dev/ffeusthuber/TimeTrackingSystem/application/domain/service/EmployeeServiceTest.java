package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.DayOfWeek;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeServiceTest {

    @Test
    void createdEmployeeGetsAssignedId() {
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withoutEmployees();
        EmployeeService employeeService = new EmployeeService(employeeRepository);

        Employee createdEmployee = employeeService.createEmployee("Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());

        assertThat(createdEmployee.getEmployeeID()).isNotNull();
    }

    @Test
    void createdEmployeeGetsAssignedTheDefaultWorkSchedule() {
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withoutEmployees();
        EmployeeService employeeService = new EmployeeService(employeeRepository);

        Employee createdEmployee = employeeService.createEmployee("Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());

        assertThat(createdEmployee.getWorkSchedule()).isEqualTo(WorkSchedule.createDefaultWorkSchedule());
    }

    @Test
    void canGetEmployeeById() {
        Employee employee = new Employee(1L, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());
        EmployeeRepository employeeRepositoryStub = EmployeeRepositoryStub.withEmployee(employee);
        EmployeeService employeeService = new EmployeeService(employeeRepositoryStub);

        Employee foundEmployee = employeeService.getEmployeeById(1L);

        assertThat(foundEmployee).isEqualTo(employee);
    }

    @Test
    void givenADayOfWeekCanGetScheduledHoursForEmployee() {
        long employeeId = 1L;
        float hoursMonday = 8;
        WorkSchedule workSchedule = WorkSchedule.createSpecificWorkSchedule(hoursMonday, 8.5f, 8.5f, 8.5f, 8.5f, 8.5f, 8.5f);
        Employee employee = new Employee(employeeId, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, workSchedule);
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withEmployee(employee);
        EmployeeService employeeService = new EmployeeService(employeeRepository);

        float actualScheduledHours = employeeService.getScheduledHoursForEmployeeOnWeekDay(employeeId, DayOfWeek.MONDAY);

        assertThat(actualScheduledHours).isEqualTo(hoursMonday);
    }

    @Test
    void canGetClockStateForEmployees() {
        long clockedInEmployeeId = 1L;
        long clockedOutEmployeeId = 2L;
        long pausedEmployeeId = 3L;
        Employee clockedInEmployee = new Employee(clockedInEmployeeId, ClockState.CLOCKED_IN, WorkSchedule.createDefaultWorkSchedule());
        Employee clockedOutEmployee = new Employee(clockedOutEmployeeId, ClockState.CLOCKED_OUT, WorkSchedule.createDefaultWorkSchedule());
        Employee pausedEmployee = new Employee(pausedEmployeeId, ClockState.ON_PAUSE, WorkSchedule.createDefaultWorkSchedule());
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
        Employee employee = new Employee(employeeId, ClockState.CLOCKED_OUT, WorkSchedule.createDefaultWorkSchedule());
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withEmployee(employee);
        EmployeeService employeeService = new EmployeeService(employeeRepository);

        employeeService.setClockStateForEmployee(employeeId, ClockState.CLOCKED_IN);

        assertThat(employeeRepository.getEmployeeByID(employeeId).getClockState()).isEqualTo(ClockState.CLOCKED_IN);
    }

    @Test
    void createdEmployeePasswordIsEncrypted() {
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withoutEmployees();
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        String rawPassword = "password";
        Employee createdEmployee = employeeService.createEmployee("Jane", "Doe", "j.doe@test-mail.com", rawPassword, EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());

        String encryptedPassword = createdEmployee.getPassword();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertThat(passwordEncoder.matches(rawPassword, encryptedPassword)).isTrue();
    }

}
