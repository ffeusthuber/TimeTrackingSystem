package dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry;

import java.time.ZonedDateTime;

public class TimeEntry implements Comparable<TimeEntry>{

    private final long workdayID;
    private final TimeEntryType entryType;
    private final ZonedDateTime entryDateTime;

    public TimeEntry(long workdayID, TimeEntryType entryType, ZonedDateTime entryDateTime) {
        this.workdayID = workdayID;
        this.entryType = entryType;
        this.entryDateTime = entryDateTime;
    }

    public long getEmployeeID() {
        return this.workdayID;
    }

    public TimeEntryType getType() {
        return this.entryType;
    }

    public ZonedDateTime getEntryDateTime() {
        return entryDateTime;
    }

    @Override
    public int compareTo(TimeEntry timeEntry) {
        return this.entryDateTime.compareTo(timeEntry.getEntryDateTime());
    }
}
