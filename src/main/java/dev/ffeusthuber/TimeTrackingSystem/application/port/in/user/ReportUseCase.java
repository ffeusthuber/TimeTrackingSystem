package dev.ffeusthuber.TimeTrackingSystem.application.port.in.user;

import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WeekReport;

import java.time.ZoneId;
import java.util.List;

public interface ReportUseCase {
    List<TimeEntryDTO> getTimeEntriesOfLatestWorkdayOfEmployee(long employeeID, ZoneId zoneId);
    WeekReport getCurrentWeekReportForEmployee(long employeeId);
}
