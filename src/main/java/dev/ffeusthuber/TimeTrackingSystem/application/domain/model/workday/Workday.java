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
        return calculateTotalHoursForTimeEntryType(TimeEntryType.CLOCK_IN);
    }

    public float calculatePausedHours() {
        return calculateTotalHoursForTimeEntryType(TimeEntryType.CLOCK_PAUSE);
    }

    private float calculateTotalHoursForTimeEntryType(TimeEntryType timeEntryType) {
        if (timeEntries.isEmpty()) return 0.0f;

        Queue<TimeEntry> entriesCopy = new LinkedList<>(timeEntries);

        float totalHours = 0.0f;
        while (entriesCopy.size() >= 2) {
            skipUntilNextTimeEntryOfType(entriesCopy, timeEntryType);
            if(entriesCopy.size() < 2) break;
            ZonedDateTime start = entriesCopy.poll().getEntryDateTime();
            ZonedDateTime end = entriesCopy.poll().getEntryDateTime();
            totalHours += calculateHoursBetween(start, end);
        }

        if(entriesCopy.size() == 1 && entriesCopy.peek().getType() == timeEntryType) {
            ZonedDateTime timeOfLastEntry = entriesCopy.poll().getEntryDateTime();
            ZonedDateTime now = ZonedDateTime.now();
            totalHours += calculateHoursBetween(timeOfLastEntry, now);
        }

        return totalHours;
    }

    private float calculateHoursBetween(ZonedDateTime start, ZonedDateTime end) {
        long secondsWorked = ChronoUnit.SECONDS.between(start, end);
        return secondsWorked / 3600.0f;
    }

    private void skipUntilNextTimeEntryOfType(Queue<TimeEntry> entriesCopy, TimeEntryType timeEntryType) {
        while (!entriesCopy.isEmpty() && entriesCopy.peek().getType() != timeEntryType) {
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