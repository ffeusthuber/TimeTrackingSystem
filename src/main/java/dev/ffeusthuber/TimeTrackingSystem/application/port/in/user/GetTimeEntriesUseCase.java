package dev.ffeusthuber.TimeTrackingSystem.application.port.in.user;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;

import java.util.List;

public interface GetTimeEntriesUseCase {
    List<TimeEntry> getTimeEntriesForEmployee(long employeeID);
}
