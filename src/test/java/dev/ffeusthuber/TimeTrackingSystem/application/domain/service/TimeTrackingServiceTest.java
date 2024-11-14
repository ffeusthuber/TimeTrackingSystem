package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;


import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.adapter.out.WorkdayRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockError;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockResponse;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.TimeTrackingUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeTrackingServiceTest {

    @Test
    void canClockInWithGivenEmployeeID() {
        long clockedOutEmployeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithEmployeeInClockState(clockedOutEmployeeID, ClockState.CLOCKED_OUT);

        ClockResponse clockResponse = timeTrackingUseCase.clockIn(clockedOutEmployeeID);

        assertThat(clockResponse).isEqualTo(new ClockResponse(clockedOutEmployeeID, TimeEntryType.CLOCK_IN));
    }

    @Test
    void canClockOutWithGivenEmployeeID() {
        long clockedInEmployeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithEmployeeInClockState(clockedInEmployeeID, ClockState.CLOCKED_IN);

        ClockResponse clockResponse = timeTrackingUseCase.clockOut(clockedInEmployeeID);

        assertThat(clockResponse).isEqualTo(new ClockResponse(clockedInEmployeeID, TimeEntryType.CLOCK_OUT));
    }

    @Test
    void canClockPauseWithGivenEmployeeID() {
        long clockedInEmployeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithEmployeeInClockState(clockedInEmployeeID, ClockState.CLOCKED_IN);

        ClockResponse clockResponse = timeTrackingUseCase.clockPause(clockedInEmployeeID);

        assertThat(clockResponse).isEqualTo(new ClockResponse(clockedInEmployeeID, TimeEntryType.CLOCK_PAUSE));
    }

    @Test
    void clockingInWhenAlreadyClockedInReturnsError() {
        long clockedInEmployeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithEmployeeInClockState(clockedInEmployeeID, ClockState.CLOCKED_IN);

        ClockResponse clockResponse = timeTrackingUseCase.clockIn(clockedInEmployeeID);

        assertThat(clockResponse).isEqualTo(new ClockResponse(clockedInEmployeeID, ClockError.EMPLOYEE_ALREADY_CLOCKED_IN));
    }

    @Test
    void clockingOutWhenNotClockedInReturnsError() {
        long clockedOutEmployeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithEmployeeInClockState(clockedOutEmployeeID, ClockState.CLOCKED_OUT);

        ClockResponse clockResponse = timeTrackingUseCase.clockOut(clockedOutEmployeeID);

        assertThat(clockResponse).isEqualTo(new ClockResponse(clockedOutEmployeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN));
    }

    @Test
    void clockingPauseWhenNotClockedInReturnsError() {
        long clockedOutEmployeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithEmployeeInClockState(clockedOutEmployeeID, ClockState.CLOCKED_OUT);

        ClockResponse clockResponse = timeTrackingUseCase.clockPause(clockedOutEmployeeID);

        assertThat(clockResponse).isEqualTo(new ClockResponse(clockedOutEmployeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN));
    }

    @Test
    void canClockInAfterClockingOut() throws InterruptedException {
        long employeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithEmployeeInClockState(employeeID, ClockState.CLOCKED_OUT);

        timeTrackingUseCase.clockIn(employeeID);
        // wait to ensure that the clock in time is different from the clock out time for comparison
        Thread.sleep(10);

        timeTrackingUseCase.clockOut(employeeID);
        ClockResponse clockResponse = timeTrackingUseCase.clockIn(employeeID);

        assertThat(clockResponse).isEqualTo(new ClockResponse(employeeID, TimeEntryType.CLOCK_IN));
    }


    @Test
    void successfulClockingAddsTimeEntryToWorkday() {
        long employeeID = 1L;
        WorkdayRepository workdayRepository = WorkdayRepositoryStub.withoutWorkdays();
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithEmployeeInClockState(employeeID, ClockState.CLOCKED_OUT, workdayRepository);

        timeTrackingUseCase.clockIn(employeeID);
        Workday workday = workdayRepository.getLatestWorkdayForEmployee(employeeID).orElseThrow();
        assertThat(workday.getTimeEntries()).hasSize(1);
        timeTrackingUseCase.clockOut(employeeID);
        assertThat(workday.getTimeEntries()).hasSize(2);
    }

    @Test
    void unsuccessfulClockingShouldNotAddTimeEntryToWorkday() {
        long employeeID = 1L;
        WorkdayRepository workdayRepository = WorkdayRepositoryStub.withoutWorkdays();
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithEmployeeInClockState(employeeID, ClockState.CLOCKED_OUT, workdayRepository);

        timeTrackingUseCase.clockIn(employeeID);
        Workday workday = workdayRepository.getLatestWorkdayForEmployee(employeeID).orElseThrow();
        assertThat(workday.getTimeEntries()).hasSize(1);
        timeTrackingUseCase.clockIn(employeeID);
        assertThat(workday.getTimeEntries()).hasSize(1);
    }

    private TimeTrackingUseCase getTimeTrackingServiceWithEmployeeInClockState(long employeeID, ClockState clockState) {
        WorkdayRepository workdayRepository = WorkdayRepositoryStub.withoutWorkdays();
        Employee employee = new Employee(employeeID, clockState, WorkSchedule.createDefaultWorkSchedule());
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withEmployee(employee);
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        WorkdayService workdayService = new WorkdayService(employeeRepository, workdayRepository);
        TimeEntryService timeEntryService = new TimeEntryService();
        return new TimeTrackingService(timeEntryService, employeeService, workdayService);
    }

    private TimeTrackingUseCase getTimeTrackingServiceWithEmployeeInClockState(long employeeID, ClockState clockState, WorkdayRepository workdayRepository) {
        Employee employee = new Employee(employeeID, clockState, WorkSchedule.createDefaultWorkSchedule());
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withEmployee(employee);
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        WorkdayService workdayService = new WorkdayService(employeeRepository, workdayRepository);
        TimeEntryService timeEntryService = new TimeEntryService();
        return new TimeTrackingService(timeEntryService, employeeService, workdayService);
    }
}
