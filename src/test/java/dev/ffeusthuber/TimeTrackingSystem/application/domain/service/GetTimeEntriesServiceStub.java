package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.GetTimeEntriesUseCase;

import java.util.List;

public class GetTimeEntriesServiceStub implements GetTimeEntriesUseCase {
    private final List<TimeEntry> timeEntriesToReturn;

    private GetTimeEntriesServiceStub(List<TimeEntry> timeEntriesToReturn) {
        this.timeEntriesToReturn = timeEntriesToReturn;
    }

    public List<TimeEntry> getTimeEntriesForEmployee(long employeeID) {
        return timeEntriesToReturn;
    }

    public static GetTimeEntriesServiceStub withTimeEntriesToReturn(List<TimeEntry> timeEntriesToReturn) {
        return new GetTimeEntriesServiceStub(timeEntriesToReturn);
    }
}
