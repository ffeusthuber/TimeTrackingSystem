package dev.ffeusthuber.TimeTrackingSystem.application.port.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;

import java.util.List;

public interface TimeEntryRepository {

    TimeEntry getCurrentTimeEntryForEmployee(long employeeID);

    List<TimeEntry> getTimeEntriesForEmployee(long employeeID);

    void save(TimeEntry timeEntry);
}
