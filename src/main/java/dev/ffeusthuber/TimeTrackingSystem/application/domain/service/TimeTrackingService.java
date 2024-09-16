package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockError;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockResponse;
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
    public ClockResponse clockIn(long employeeID) {
        if (isEmployeeClockedIn(employeeID)) {
            return ClockResponse.error(employeeID, ClockError.EMPLOYEE_ALREADY_CLOCKED_IN);
        }
        // save time entry
        return ClockResponse.success(employeeID, TimeEntryType.CLOCK_IN);
    }

    @Override
    public ClockResponse clockOut(long employeeID) {
        if (!isEmployeeClockedIn(employeeID)) {
            return ClockResponse.error(employeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN);
        }
        return ClockResponse.success(employeeID, TimeEntryType.CLOCK_OUT);
    }

    @Override
    public ClockResponse clockPause(long employeeID) {
        if(!isEmployeeClockedIn(employeeID)) {
            return ClockResponse.error(employeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN);
        }
        return ClockResponse.success(employeeID, TimeEntryType.CLOCK_PAUSE);
    }

    private TimeEntry createTimeEntry(long employeeID, TimeEntryType timeEntryType) {
        return new TimeEntry(employeeID, timeEntryType, ZonedDateTime.now(ZoneOffset.UTC));
    }

    private boolean isEmployeeClockedIn(long employeeID) {
        return employeeClockStatusService.checkEmployeeClockStatus(employeeID) == TimeEntryType.CLOCK_IN;
    }

}
