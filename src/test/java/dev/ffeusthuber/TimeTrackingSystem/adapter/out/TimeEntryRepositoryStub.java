package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.TimeEntryRepository;
import org.springframework.stereotype.Repository;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TimeEntryRepositoryStub implements TimeEntryRepository {

    private final List<TimeEntry> timeEntries = new ArrayList<>();

    private TimeEntryRepositoryStub() {
    }

    private TimeEntryRepositoryStub(TimeEntry timeEntry) {
        this.timeEntries.add(timeEntry);
    }

    private TimeEntryRepositoryStub(List<TimeEntry> timeEntries) {
       this.timeEntries.addAll(timeEntries);
    }

    @Override
    public TimeEntry getCurrentTimeEntryForEmployee(long employeeID) {
        return timeEntries.stream()
                .filter(timeEntry -> timeEntry.getEmployeeID() == employeeID)
                .max(TimeEntry::compareTo)
                .orElse(null);
    }

    @Override
    public List<TimeEntry> getTimeEntriesForEmployee(long employeeID) {
        return timeEntries.stream()
                .filter(timeEntry -> timeEntry.getEmployeeID() == employeeID)
                .toList();
    }

    @Override
    public void save(TimeEntry timeEntry) {
        timeEntries.add(timeEntry);
    }

    public static TimeEntryRepositoryStub withoutEntries() {
        return new TimeEntryRepositoryStub();
    }

    public static TimeEntryRepositoryStub withClockedInEmployee(long employeeIdOfClockedInEmployee) {
        return new TimeEntryRepositoryStub(
                new TimeEntry(employeeIdOfClockedInEmployee, TimeEntryType.CLOCK_IN, ZonedDateTime.now(ZoneOffset.UTC))
        );
    }

    public static TimeEntryRepositoryStub withEntries(List<TimeEntry> timeEntries) {
        return new TimeEntryRepositoryStub(timeEntries);
    }
}
