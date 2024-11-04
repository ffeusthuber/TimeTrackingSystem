package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;

import java.time.ZoneId;
import java.util.List;

public class ReportServiceStub implements ReportUseCase {
    private final List<TimeEntry> timeEntriesToReturn;

    private ReportServiceStub(List<TimeEntry> timeEntriesToReturn) {
        this.timeEntriesToReturn = timeEntriesToReturn;
    }

    public List<TimeEntryDTO> displayTimeEntriesOfEmployee(long employeeID, ZoneId zoneId) {
        return timeEntriesToReturn.stream()
                .map(timeEntry -> new TimeEntryDTO(timeEntry,zoneId))
                .toList();
    }

    public static ReportServiceStub withTimeEntriesToReturn(List<TimeEntry> timeEntriesToReturn) {
        return new ReportServiceStub(timeEntriesToReturn);
    }
}
