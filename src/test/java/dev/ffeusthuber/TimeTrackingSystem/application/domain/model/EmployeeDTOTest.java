package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeDTOTest {
    @Test
    void employeeGetsConvertedToDTOCorrectly() {
        Employee employee = new Employee(1L, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER);

        EmployeeDTO employeeDTO = new EmployeeDTO(employee);

        assertThat(employeeDTO.getFullName()).isEqualTo("Jane Doe");
        assertThat(employeeDTO.getRole()).isEqualTo("USER");
    }
}
