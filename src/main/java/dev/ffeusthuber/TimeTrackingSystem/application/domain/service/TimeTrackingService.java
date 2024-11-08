package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.*;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.TimeTrackingUseCase;
import org.springframework.stereotype.Service;

@Service
public class TimeTrackingService implements TimeTrackingUseCase {
    private final TimeEntryService timeEntryService;
    private final EmployeeService employeeService;
    private final WorkdayService workdayService;

    public TimeTrackingService(TimeEntryService timeEntryService, EmployeeService employeeService, WorkdayService workdayService) {
        this.timeEntryService = timeEntryService;
        this.employeeService = employeeService;
        this.workdayService = workdayService;
    }

    @Override
    public ClockResponse clockIn(long employeeID) {
        if (employeeService.isEmployeeClockedIn(employeeID)) {
            return ClockResponse.error(employeeID, ClockError.EMPLOYEE_ALREADY_CLOCKED_IN);
        }
        return handleClockAction(employeeID, TimeEntryType.CLOCK_IN, ClockState.CLOCKED_IN);
    }

    @Override
    public ClockResponse clockOut(long employeeID) {
        if (!employeeService.isEmployeeClockedIn(employeeID)) {
            return ClockResponse.error(employeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN);
        }
        return handleClockAction(employeeID, TimeEntryType.CLOCK_OUT, ClockState.CLOCKED_OUT);
    }

    @Override
    public ClockResponse clockPause(long employeeID) {
        if(!employeeService.isEmployeeClockedIn(employeeID)) {
            return ClockResponse.error(employeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN);
        }
        return handleClockAction(employeeID, TimeEntryType.CLOCK_PAUSE, ClockState.ON_PAUSE);
    }

    private ClockResponse handleClockAction(long employeeID, TimeEntryType entryType, ClockState clockState) {
        TimeEntry timeEntry = timeEntryService.createTimeEntry(employeeID, entryType);
        workdayService.addTimeEntryToWorkday(timeEntry);
        employeeService.setClockStateForEmployee(employeeID, clockState);

        return ClockResponse.success(employeeID, entryType);
    }

}
