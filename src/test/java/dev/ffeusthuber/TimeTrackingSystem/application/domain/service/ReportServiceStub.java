package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WorkdayDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public class ReportServiceStub implements ReportUseCase {
    private final List<TimeEntry> timeEntriesToReturn;

    private ReportServiceStub(List<TimeEntry> timeEntriesToReturn) {
        this.timeEntriesToReturn = timeEntriesToReturn;
    }

    public List<TimeEntryDTO> getTimeEntriesOfEmployee(long employeeID, ZoneId zoneId) {
        return timeEntriesToReturn.stream()
                .map(timeEntry -> new TimeEntryDTO(timeEntry,zoneId))
                .toList();
    }

    @Override
    public Optional<WorkdayDTO> getLatestWorkdayOfEmployee(long employeeID) {
        return Optional.empty();
    }
}
