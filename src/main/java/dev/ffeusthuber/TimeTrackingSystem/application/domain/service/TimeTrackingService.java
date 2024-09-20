package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.*;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.TimeTrackingUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.TimeEntryRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
public class TimeTrackingService implements TimeTrackingUseCase {
    private final TimeEntryRepository timeEntryRepository;
    private final EmployeeService employeeService;

    public TimeTrackingService(TimeEntryRepository timeEntryRepository, EmployeeService employeeService) {
        this.timeEntryRepository = timeEntryRepository;
        this.employeeService = employeeService;
    }


    @Override
    public ClockResponse clockIn(long employeeID) {
        if (isEmployeeClockedIn(employeeID)) {
            return ClockResponse.error(employeeID, ClockError.EMPLOYEE_ALREADY_CLOCKED_IN);
        }
        TimeEntry timeEntry = createTimeEntry(employeeID, TimeEntryType.CLOCK_IN);
        timeEntryRepository.save(timeEntry);
        employeeService.setClockStateForEmployee(employeeID, ClockState.CLOCKED_IN);
        return ClockResponse.success(employeeID, TimeEntryType.CLOCK_IN);
    }

    @Override
    public ClockResponse clockOut(long employeeID) {
        if (!isEmployeeClockedIn(employeeID)) {
            return ClockResponse.error(employeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN);
        }
        TimeEntry timeEntry = createTimeEntry(employeeID, TimeEntryType.CLOCK_OUT);
        timeEntryRepository.save(timeEntry);
        employeeService.setClockStateForEmployee(employeeID, ClockState.CLOCKED_OUT);
        return ClockResponse.success(employeeID, TimeEntryType.CLOCK_OUT);
    }

    @Override
    public ClockResponse clockPause(long employeeID) {
        if(!isEmployeeClockedIn(employeeID)) {
            return ClockResponse.error(employeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN);
        }
        TimeEntry timeEntry = createTimeEntry(employeeID, TimeEntryType.CLOCK_PAUSE);
        timeEntryRepository.save(timeEntry);
        employeeService.setClockStateForEmployee(employeeID, ClockState.CLOCKED_OUT);
        return ClockResponse.success(employeeID, TimeEntryType.CLOCK_PAUSE);
    }

    private TimeEntry createTimeEntry(long employeeID, TimeEntryType timeEntryType) {
        return new TimeEntry(employeeID, timeEntryType, ZonedDateTime.now(ZoneOffset.UTC));
    }

    private boolean isEmployeeClockedIn(long employeeID) {
        return employeeService.getClockStateForEmployee(employeeID) == ClockState.CLOCKED_IN;
    }

}
