package dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Workday {
    private final long employeeId;
    private final LocalDate workDate;
    private final Queue<TimeEntry> timeEntries = new LinkedList<>();
    private final float scheduledHours;

    public Workday(long employeeId, LocalDate workDate, float scheduledHours) {
        this.employeeId = employeeId;
        this.workDate = workDate;
        this.scheduledHours = scheduledHours;
    }

    public long getEmployeeId() {
        return this.employeeId;
    }

    public LocalDate getWorkDate() {
        return this.workDate;
    }

    public void addTimeEntry(TimeEntry timeEntry) {
        this.timeEntries.add(timeEntry);
    }

    public List<TimeEntry> getTimeEntries() {
        return new LinkedList<>(timeEntries);
    }

    public float calculateWorkedHours() {
        if (timeEntries.isEmpty()) return 0.0f;

        Queue<TimeEntry> entriesCopy = new LinkedList<>(timeEntries);

        skipUntilFirstClockInTimeEntry(entriesCopy);

        float totalWorkedHours = 0.0f;
        while (entriesCopy.size() >= 2) {
            ZonedDateTime start = entriesCopy.poll().getEntryDateTime();
            ZonedDateTime end = entriesCopy.poll().getEntryDateTime();
            long minutesWorked = ChronoUnit.MINUTES.between(start, end);
            totalWorkedHours += minutesWorked / 60.0;
        }

        return totalWorkedHours;
    }

    private static void skipUntilFirstClockInTimeEntry(Queue<TimeEntry> entriesCopy) {
        while (entriesCopy.peek().getType() != TimeEntryType.CLOCK_IN) {
            entriesCopy.poll();
        }
    }

    public float getScheduledHours() {
        return this.scheduledHours;
    }
}