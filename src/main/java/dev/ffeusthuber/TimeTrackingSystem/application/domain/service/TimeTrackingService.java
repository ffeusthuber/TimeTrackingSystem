package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.TimeTrackingUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.TimeEntryRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
public class TimeTrackingService implements TimeTrackingUseCase {
    private final TimeEntryRepository timeEntryRepository;
    private final EmployeeClockStatusService employeeClockStatusService;

    public TimeTrackingService(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
        this.employeeClockStatusService = new EmployeeClockStatusService(timeEntryRepository);
    }

    @Override
    public TimeEntry clockIn(long employeeID) {
        if (isEmployeeAlreadyClockedIn(employeeID)) {
            return null;
        }
        return createTimeEntry(employeeID, TimeEntryType.CLOCK_IN);
    }

    @Override
    public TimeEntry clockOut(long employeeID) {
        return createTimeEntry(employeeID, TimeEntryType.CLOCK_OUT);
    }

    @Override
    public TimeEntry clockPause(long employeeID) {
        return createTimeEntry(employeeID, TimeEntryType.CLOCK_PAUSE);
    }

    private TimeEntry createTimeEntry(long employeeID, TimeEntryType timeEntryType) {
        return new TimeEntry(employeeID, timeEntryType, ZonedDateTime.now(ZoneOffset.UTC));
    }

    private boolean isEmployeeAlreadyClockedIn(long employeeID) {
        return employeeClockStatusService.checkEmployeeClockStatus(employeeID) == TimeEntryType.CLOCK_IN;
    }
}
