package dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Queue;

public class Workday {
    private final long employeeId;
    private final ZoneId timeZoneId;
    private final Queue<TimeEntry> timeEntries = new LinkedList<>();

    public Workday(long employeeId, ZoneId timeZoneId) {
        this.employeeId = employeeId;
        this.timeZoneId = timeZoneId;
    }

    public ZoneId getTimeZoneId() {
        return this.timeZoneId;
    }

    public long getEmployeeId() {
        return this.employeeId;
    }

    public void addTimeEntry(TimeEntry timeEntry) {
        this.timeEntries.add(timeEntry);
    }

    public double calculateWorkedHours() {
        if (timeEntries.isEmpty()) {
            return 0.0;
        }

        Queue<TimeEntry> entriesCopy = new LinkedList<>(timeEntries);

        while (entriesCopy.peek().getType() != TimeEntryType.CLOCK_IN) {
            entriesCopy.poll();
        }

        double totalWorkedHours = 0.0;
        while (entriesCopy.size() >= 2) {
            ZonedDateTime start = entriesCopy.poll().getEntryDateTime();
            ZonedDateTime end = entriesCopy.poll().getEntryDateTime();
            long minutesWorked = ChronoUnit.MINUTES.between(start, end);
            totalWorkedHours += minutesWorked / 60.0;
        }

        return totalWorkedHours;
    }
}