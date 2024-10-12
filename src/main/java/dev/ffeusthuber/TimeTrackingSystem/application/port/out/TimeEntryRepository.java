package dev.ffeusthuber.TimeTrackingSystem.application.port.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;

import java.util.List;

public interface TimeEntryRepository {

    List<TimeEntry> getTimeEntriesByEmployeeId(long employeeID);

    void save(TimeEntry timeEntry);
}
