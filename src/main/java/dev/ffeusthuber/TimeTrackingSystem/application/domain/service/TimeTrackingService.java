package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.timeTrackingUseCase.ClockError;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.timeTrackingUseCase.ClockResponse;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.timeTrackingUseCase.TimeTrackingUseCase;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
            return new ClockResponse(employeeID, ClockError.EMPLOYEE_ALREADY_CLOCKED_IN);
        }
        return handleClockAction(employeeID, TimeEntryType.CLOCK_IN, ClockState.CLOCKED_IN);
    }

    @Override
    public ClockResponse clockOut(long employeeID) {
        if (!employeeService.isEmployeeClockedIn(employeeID)) {
            return new ClockResponse(employeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN);
        }
        return handleClockAction(employeeID, TimeEntryType.CLOCK_OUT, ClockState.CLOCKED_OUT);
    }

    @Override
    public ClockResponse clockPause(long employeeID) {
        if(!employeeService.isEmployeeClockedIn(employeeID)) {
            return new ClockResponse(employeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN);
        }
        return handleClockAction(employeeID, TimeEntryType.CLOCK_PAUSE, ClockState.ON_PAUSE);
    }

    private ClockResponse handleClockAction(long employeeID, TimeEntryType entryType, ClockState clockState) {
        Workday workday = workdayService.getOrCreateWorkdayForEmployeeOnDate(employeeID, LocalDate.now());
        long workdayID = workday.getWorkdayId();
        TimeEntry timeEntry = timeEntryService.createTimeEntry(workdayID, entryType);
        workdayService.addTimeEntryToWorkday(timeEntry, workday);
        employeeService.setClockStateForEmployee(employeeID, clockState);

        return new ClockResponse(employeeID, entryType);
    }

}
