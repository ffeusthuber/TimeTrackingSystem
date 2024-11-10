package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WorkdayDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;

@Service
public class ReportService implements ReportUseCase {
    private final WorkdayService workdayService;

    public ReportService(WorkdayService workdayService) {
        this.workdayService = workdayService;
    }

    @Override
    public List<TimeEntryDTO> getTimeEntriesOfLatestWorkdayOfEmployee(long employeeID, ZoneId zoneId) {
        return workdayService.getLatestWorkdayForEmployee(employeeID).getTimeEntries().stream()
                .map(timeEntry -> new TimeEntryDTO(timeEntry,zoneId))
                .toList();
    }

    @Override
    public WorkdayDTO getLatestWorkdayOfEmployee(long employeeID) {
       return new WorkdayDTO(this.workdayService.getLatestWorkdayForEmployee(employeeID));
    }
}
