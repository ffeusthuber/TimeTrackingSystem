package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.GetTimeEntriesUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.TimeEntryRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class TimeEntryService implements GetTimeEntriesUseCase {
    private final TimeEntryRepository timeEntryRepository;

    public TimeEntryService(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    public List<TimeEntry> getTimeEntriesForEmployee(long employeeID) {
        return this.timeEntryRepository.getTimeEntriesByEmployeeId(employeeID);
    }

    public TimeEntry createTimeEntry(long employeeID, TimeEntryType timeEntryType) {
        TimeEntry timeEntry = new TimeEntry(employeeID, timeEntryType, ZonedDateTime.now(ZoneOffset.UTC));
        timeEntryRepository.save(timeEntry);
        return timeEntry;
    }
}
