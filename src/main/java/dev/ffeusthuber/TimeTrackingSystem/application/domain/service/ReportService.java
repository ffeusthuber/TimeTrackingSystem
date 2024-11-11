package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WorkdayDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService implements ReportUseCase {
    private final WorkdayService workdayService;

    public ReportService(WorkdayService workdayService) {
        this.workdayService = workdayService;
    }

    @Override
    public List<TimeEntryDTO> getTimeEntriesOfLatestWorkdayOfEmployee(long employeeID, ZoneId zoneId) {
        Optional<Workday> optionalWorkday = this.workdayService.getLatestWorkdayForEmployee(employeeID);
        return optionalWorkday.map(workday -> workday.getTimeEntries().stream()
                                                     .map(timeEntry -> new TimeEntryDTO(timeEntry, zoneId))
                                                     .toList()).orElse(Collections.emptyList());
    }

    @Override
    public WorkdayDTO getLatestWorkdayOfEmployee(long employeeID) {
        Optional< Workday> optionalWorkday = this.workdayService.getLatestWorkdayForEmployee(employeeID);
        return optionalWorkday.map(WorkdayDTO::new).orElse(null);
    }
}
