package dev.ffeusthuber.TimeTrackingSystem.application.port.in.user;

import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;

import java.time.ZoneId;
import java.util.List;

public interface GetTimeEntriesUseCase {
    List<TimeEntryDTO> getTimeEntriesForEmployee(long employeeID, ZoneId zoneId);
}
