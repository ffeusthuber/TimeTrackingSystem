package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

public class TimeEntry {

    private final long employeeID;
    private final TimeEntryType timeEntryType;

    public TimeEntry(long employeeID, TimeEntryType timeEntryType) {
        this.employeeID = employeeID;
        this.timeEntryType = timeEntryType;
    }

    public long getEmployeeID() {
        return this.employeeID;
    }

    public TimeEntryType getType() {
        return this.timeEntryType;
    }
}
