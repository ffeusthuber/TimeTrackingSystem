package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WorkdayDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class ReportServiceStub implements ReportUseCase {
    private final List<TimeEntry> timeEntriesToReturn;

    private ReportServiceStub(List<TimeEntry> timeEntriesToReturn) {
        this.timeEntriesToReturn = timeEntriesToReturn;
    }

    public List<TimeEntryDTO> getTimeEntriesOfLatestWorkdayOfEmployee(long employeeID, ZoneId zoneId) {
        return timeEntriesToReturn.stream()
                .map(timeEntry -> new TimeEntryDTO(timeEntry,zoneId))
                .toList();
    }

    @Override
    public WorkdayDTO getLatestWorkdayOfEmployee(long employeeID) {
        return new WorkdayDTO(new Workday(1L, LocalDate.now(), 8.5f));
    }
}
