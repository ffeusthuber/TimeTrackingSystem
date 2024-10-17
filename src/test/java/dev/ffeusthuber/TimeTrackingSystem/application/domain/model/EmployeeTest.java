package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.config.WorkScheduleConfig;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeTest {

    @Test
    void newEmployeeGetsAssignedClockedOutState() {
        Long employeeID = 1L;
        WorkScheduleConfig workScheduleConfig = new WorkScheduleConfig();
        Employee employee = new Employee(employeeID, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, workScheduleConfig.defaultWorkSchedule());

        assertThat(employee.getClockState()).isEqualTo(ClockState.CLOCKED_OUT);
    }
}
