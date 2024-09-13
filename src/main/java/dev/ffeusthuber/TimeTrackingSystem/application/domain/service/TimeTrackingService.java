package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.TimeTrackingUseCase;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
public class TimeTrackingService implements TimeTrackingUseCase {
    @Override
    public TimeEntry clockIn(long employeeID) {
        return new TimeEntry(employeeID, TimeEntryType.CLOCK_IN, ZonedDateTime.now(ZoneOffset.UTC));
    }

    @Override
    public TimeEntry clockOut(long employeeID) {
        return new TimeEntry(employeeID, TimeEntryType.CLOCK_OUT, ZonedDateTime.now(ZoneOffset.UTC));
    }

    @Override
    public TimeEntry clockPause(long employeeID) {
        return new TimeEntry(employeeID, TimeEntryType.CLOCK_PAUSE, ZonedDateTime.now(ZoneOffset.UTC));
    }
}
