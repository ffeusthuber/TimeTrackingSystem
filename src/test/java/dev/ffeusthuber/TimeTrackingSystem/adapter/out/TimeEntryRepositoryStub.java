package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.TimeEntryRepository;
import org.springframework.stereotype.Repository;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static java.util.Collections.emptyList;

@Repository
public class TimeEntryRepositoryStub implements TimeEntryRepository {

    private final List<TimeEntry> timeEntries;

    private TimeEntryRepositoryStub() {
        this.timeEntries = emptyList();
    }

    private TimeEntryRepositoryStub(TimeEntry timeEntry) {
        this.timeEntries = List.of(timeEntry);
    }

    private TimeEntryRepositoryStub(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries;
    }

    @Override
    public TimeEntry getCurrentTimeEntryForEmployee(long employeeID) {
        return timeEntries.stream()
                .filter(timeEntry -> timeEntry.getEmployeeID() == employeeID)
                .findFirst()
                .orElse(null);
    }

    public static TimeEntryRepository withoutEntries() {
        return new TimeEntryRepositoryStub();
    }

    public static TimeEntryRepository withClockedInEmployee(long employeeIdOfClockedInEmployee) {
        return new TimeEntryRepositoryStub(
                new TimeEntry(employeeIdOfClockedInEmployee, TimeEntryType.CLOCK_IN, ZonedDateTime.now(ZoneOffset.UTC))
        );
    }

    public static TimeEntryRepository withEntries(List<TimeEntry> timeEntries) {
        return new TimeEntryRepositoryStub(timeEntries);
    }
}
