package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeTest {

    @Test
    void newEmployeeGetsAssignedClockedOutState() {
        Long employeeID = 1L;
        Employee employee = new Employee(employeeID, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());

        assertThat(employee.getClockState()).isEqualTo(ClockState.CLOCKED_OUT);
    }
}
