package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

public class Employee {
    private final Long employeeID;
    private ClockState clockState;

    public Employee(Long employeeID) {
        this.employeeID = employeeID;
        this.clockState = ClockState.CLOCKED_OUT;
    }

    public Employee(Long employeeID, ClockState clockState) {
        this.employeeID = employeeID;
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
}
