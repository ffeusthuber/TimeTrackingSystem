package dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockState;

import java.util.Objects;

public class Employee {

    private final Long employeeID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private EmployeeRole role;
    private ClockState clockState;

    public Employee(Long employeeID, ClockState clockState) {
        this.employeeID = employeeID;
        this.clockState = clockState;
    }

    public Employee(Long employeeID, String firstName, String lastName, String email, String password, EmployeeRole role) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.clockState = ClockState.CLOCKED_OUT;
    }

    public Employee(Long employeeID, String firstName, String lastName, String email, String password, EmployeeRole role, ClockState clockState) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.clockState = clockState;
    }

    public Long getEmployeeID() {
        return this.employeeID;
    }

    public ClockState getClockState() {
        return this.clockState;
    }

    public void clockIn() {
        this.clockState = ClockState.CLOCKED_IN;
    }

    public void clockOut() {
        this.clockState = ClockState.CLOCKED_OUT;
    }

    public void clockPause() {
        this.clockState = ClockState.ON_PAUSE;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public EmployeeRole getRole() {
        return this.role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return Objects.equals(employeeID, employee.employeeID) && Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName) && Objects.equals(email, employee.email) && Objects.equals(password, employee.password) && role == employee.role && clockState == employee.clockState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeID, firstName, lastName, email, password, role, clockState);
    }
}
