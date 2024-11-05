package dev.ffeusthuber.TimeTrackingSystem.application.port.in.user;

import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WorkdayDTO;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public interface ReportUseCase {
    List<TimeEntryDTO> displayTimeEntriesOfEmployee(long employeeID, ZoneId zoneId);
    Optional<WorkdayDTO> displayLatestWorkdayOfEmployee(long employeeID);
}
