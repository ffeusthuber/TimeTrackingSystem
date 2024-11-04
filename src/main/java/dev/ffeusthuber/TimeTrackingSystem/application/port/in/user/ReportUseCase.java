package dev.ffeusthuber.TimeTrackingSystem.application.port.in.user;

import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;

import java.time.ZoneId;
import java.util.List;

public interface ReportUseCase {
    List<TimeEntryDTO> displayTimeEntriesOfEmployee(long employeeID, ZoneId zoneId);
}
