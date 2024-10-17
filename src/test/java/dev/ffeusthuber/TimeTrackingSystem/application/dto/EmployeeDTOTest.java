package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.config.WorkScheduleConfig;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeDTOTest {
    @Test
    void employeeGetsConvertedToDTOCorrectly() {
        WorkScheduleConfig workScheduleConfig = new WorkScheduleConfig();
        Employee employee = new Employee(1L, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, workScheduleConfig.defaultWorkSchedule());

        EmployeeDTO employeeDTO = new EmployeeDTO(employee);

        assertThat(employeeDTO.getFullName()).isEqualTo("Jane Doe");
        assertThat(employeeDTO.getRole()).isEqualTo("USER");
    }
}
