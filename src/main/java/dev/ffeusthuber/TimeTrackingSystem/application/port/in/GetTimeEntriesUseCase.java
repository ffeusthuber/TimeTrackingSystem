package dev.ffeusthuber.TimeTrackingSystem.application.port.in;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;

import java.util.List;

public interface GetTimeEntriesUseCase {
    List<TimeEntry> getTimeEntriesForEmployee(long employeeID);
}
