package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.TimeTrackingUseCase;

public class TimeTrackingService implements TimeTrackingUseCase {
    @Override
    public TimeEntry clockIn(long employeeID) {
        return new TimeEntry(employeeID, TimeEntryType.CLOCK_IN);
    }
}
