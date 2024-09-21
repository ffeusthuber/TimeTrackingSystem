package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockError;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockResponse;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.TimeTrackingUseCase;
import org.springframework.stereotype.Service;

@Service
public class TimeTrackingService implements TimeTrackingUseCase {
    private final TimeEntryService timeEntryService;
    private final EmployeeService employeeService;

    public TimeTrackingService(TimeEntryService timeEntryService, EmployeeService employeeService) {
        this.timeEntryService = timeEntryService;
        this.employeeService = employeeService;
    }

    @Override
    public ClockResponse clockIn(long employeeID) {
        if (employeeService.isEmployeeClockedIn(employeeID)) {
            return ClockResponse.error(employeeID, ClockError.EMPLOYEE_ALREADY_CLOCKED_IN);
        }
        timeEntryService.createTimeEntry(employeeID, TimeEntryType.CLOCK_IN);
        employeeService.setClockStateForEmployee(employeeID, ClockState.CLOCKED_IN);
        return ClockResponse.success(employeeID, TimeEntryType.CLOCK_IN);
    }

    @Override
    public ClockResponse clockOut(long employeeID) {
        if (!employeeService.isEmployeeClockedIn(employeeID)) {
            return ClockResponse.error(employeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN);
        }
        timeEntryService.createTimeEntry(employeeID, TimeEntryType.CLOCK_OUT);
        employeeService.setClockStateForEmployee(employeeID, ClockState.CLOCKED_OUT);
        return ClockResponse.success(employeeID, TimeEntryType.CLOCK_OUT);
    }

    @Override
    public ClockResponse clockPause(long employeeID) {
        if(!employeeService.isEmployeeClockedIn(employeeID)) {
            return ClockResponse.error(employeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN);
        }
        timeEntryService.createTimeEntry(employeeID, TimeEntryType.CLOCK_PAUSE);
        employeeService.setClockStateForEmployee(employeeID, ClockState.CLOCKED_OUT);
        return ClockResponse.success(employeeID, TimeEntryType.CLOCK_PAUSE);
    }

}
