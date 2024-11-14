package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;

public record EmployeeDTO(String fullName, String role) {
    public EmployeeDTO(Employee employee) {
        this(employee.getFirstName() + " " + employee.getLastName(), employee.getRole().toString());
    }
}
