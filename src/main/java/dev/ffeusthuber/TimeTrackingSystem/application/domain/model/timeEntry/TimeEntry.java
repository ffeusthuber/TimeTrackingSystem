package dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry;

import java.time.ZonedDateTime;

public record TimeEntry(
        long workdayID,
        TimeEntryType entryType,
        ZonedDateTime entryDateTime) implements Comparable<TimeEntry> {

    @Override
    public int compareTo(TimeEntry other) {
        return this.entryDateTime.compareTo(other.entryDateTime());
    }
}