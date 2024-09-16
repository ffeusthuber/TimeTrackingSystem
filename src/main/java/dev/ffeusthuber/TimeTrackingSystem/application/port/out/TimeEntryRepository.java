package dev.ffeusthuber.TimeTrackingSystem.application.port.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;

public interface TimeEntryRepository {

    TimeEntry getCurrentTimeEntryForEmployee(long employeeID);
}
