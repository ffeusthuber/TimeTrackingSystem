package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WorkdayDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public WorkdayDTO getWorkdayDataForLatestWorkdayOfEmployee(long employeeID) {
        Optional< Workday> optionalWorkday = this.workdayService.getLatestWorkdayForEmployee(employeeID);
        return optionalWorkday.map(WorkdayDTO::new).orElse(null);
    }

    @Override
    public List<WorkdayDTO> getWorkdayDataForCurrentWeekForEmployee(long employeeId1) {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = now.with(java.time.DayOfWeek.SUNDAY);
        return workdayService.getWorkdaysForEmployeeBetweenDates(employeeId1, startOfWeek, endOfWeek).stream()
                             .map(WorkdayDTO::new)
                             .toList();
    }
}
