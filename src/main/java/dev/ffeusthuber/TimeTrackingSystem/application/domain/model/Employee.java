package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

public class Employee {

    private Long employeeID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private EmployeeRole role;
    private ClockState clockState;

    public Employee(Long employeeID) {
        this.employeeID = employeeID;
        this.clockState = ClockState.CLOCKED_OUT;
    }

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
}
