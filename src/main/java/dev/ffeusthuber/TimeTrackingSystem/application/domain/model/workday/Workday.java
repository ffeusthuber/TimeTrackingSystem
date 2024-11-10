package dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class Workday {
    private long workdayId;
    private final long employeeId;
    private final LocalDate workDate;
    private final Queue<TimeEntry> timeEntries = new LinkedList<>();
    private final float scheduledHours;

    public Workday(long workdayId, long employeeId, LocalDate workDate, float scheduledHours, List<TimeEntry> timeEntries) {
        this.workdayId = workdayId;
        this.employeeId = employeeId;
        this.workDate = workDate;
        this.scheduledHours = scheduledHours;
        this.timeEntries.addAll(timeEntries);
    }

    public Workday(long employeeId, LocalDate workDate, float scheduledHours) {
        this.employeeId = employeeId;
        this.workDate = workDate;
        this.scheduledHours = scheduledHours;
    }

    public long getWorkdayId() {
        return this.workdayId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Workday workday)) return false;
        return employeeId == workday.employeeId && Float.compare(scheduledHours, workday.scheduledHours) == 0 && Objects.equals(workDate, workday.workDate) && Objects.equals(timeEntries, workday.timeEntries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, workDate, timeEntries, scheduledHours);
    }
}