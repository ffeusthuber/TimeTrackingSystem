package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeDTOTest {
    @Test
    void employeeGetsConvertedToDTOCorrectly() {
        Employee employee = new Employee(1L, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());

        EmployeeDTO employeeDTO = new EmployeeDTO(employee);

        assertThat(employeeDTO.employeeId()).isEqualTo(1L);
        assertThat(employeeDTO.fullName()).isEqualTo("Jane Doe");
        assertThat(employeeDTO.role()).isEqualTo("USER");
        assertThat(employeeDTO.clockState()).isEqualTo("Clocked out");
    }
}
