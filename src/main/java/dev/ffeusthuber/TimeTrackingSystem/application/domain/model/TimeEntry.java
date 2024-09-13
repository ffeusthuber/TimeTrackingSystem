package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

import java.time.ZonedDateTime;

public class TimeEntry {

    private final long employeeID;
    private final TimeEntryType entryType;
    private final ZonedDateTime entryDateTime;

    public TimeEntry(long employeeID, TimeEntryType entryType, ZonedDateTime entryDateTime) {
        this.employeeID = employeeID;
        this.entryType = entryType;
        this.entryDateTime = entryDateTime;
    }

    public long getEmployeeID() {
        return this.employeeID;
    }

    public TimeEntryType getType() {
        return this.entryType;
    }

    public ZonedDateTime getEntryDateTime() {
        return entryDateTime;
    }
}
