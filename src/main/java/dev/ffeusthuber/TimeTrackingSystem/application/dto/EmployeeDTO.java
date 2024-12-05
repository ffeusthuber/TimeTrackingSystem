package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockState;

public record EmployeeDTO(String fullName, String role, ClockState clockState) {

    public EmployeeDTO(Employee employee) {
        this(employee.getFirstName() + " " + employee.getLastName(),
             employee.getRole().toString(),
             employee.getClockState());
    }
}
