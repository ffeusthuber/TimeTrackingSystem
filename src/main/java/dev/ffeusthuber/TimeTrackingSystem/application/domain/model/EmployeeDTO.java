package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

public class EmployeeDTO {
    private final String fullName;
    private final String role;

    public EmployeeDTO(Employee employee) {
        this.fullName = employee.getFirstName() + " " + employee.getLastName();
        this.role = employee.getRole().toString();
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getRole() {
        return this.role;
    }
}
