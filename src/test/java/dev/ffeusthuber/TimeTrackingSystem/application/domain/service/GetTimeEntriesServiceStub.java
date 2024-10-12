package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.GetTimeEntriesUseCase;

import java.time.ZoneId;
import java.util.List;

public class GetTimeEntriesServiceStub implements GetTimeEntriesUseCase {
    private final List<TimeEntry> timeEntriesToReturn;

    private GetTimeEntriesServiceStub(List<TimeEntry> timeEntriesToReturn) {
        this.timeEntriesToReturn = timeEntriesToReturn;
    }

    public List<TimeEntryDTO> getTimeEntriesForEmployee(long employeeID, ZoneId zoneId) {
        return timeEntriesToReturn.stream()
                .map(timeEntry -> new TimeEntryDTO(timeEntry,zoneId))
                .toList();
    }

    public static GetTimeEntriesServiceStub withTimeEntriesToReturn(List<TimeEntry> timeEntriesToReturn) {
        return new GetTimeEntriesServiceStub(timeEntriesToReturn);
    }
}
