package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;

public record EmployeeDTO(String fullName, String role, String clockState) {

    public EmployeeDTO(Employee employee) {
        this(employee.getFirstName() + " " + employee.getLastName(),
             employee.getRole().toString(),
             switch (employee.getClockState()) {
                 case CLOCKED_IN -> "Clocked in";
                 case CLOCKED_OUT -> "Clocked out";
                 case ON_PAUSE -> "On Pause";
             });
    }
}
