package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;


import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.adapter.out.TimeEntryRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockError;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockResponse;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.TimeTrackingUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.TimeEntryRepository;
import dev.ffeusthuber.TimeTrackingSystem.config.WorkScheduleConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeTrackingServiceTest {

    private static WorkScheduleConfig workScheduleConfig;

    @BeforeAll
    static void setUp() {
        workScheduleConfig = new WorkScheduleConfig();
    }

    @Test
    void canClockInWithGivenEmployeeID() {
        long clockedOutEmployeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithClockedOutEmployee(clockedOutEmployeeID);

        ClockResponse clockResponse = timeTrackingUseCase.clockIn(clockedOutEmployeeID);

        assertThat(clockResponse).isEqualTo(ClockResponse.success(clockedOutEmployeeID, TimeEntryType.CLOCK_IN));
    }

    @Test
    void canClockOutWithGivenEmployeeID() {
        long clockedInEmployeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithClockedInEmployee(clockedInEmployeeID);

        ClockResponse clockResponse = timeTrackingUseCase.clockOut(clockedInEmployeeID);

        assertThat(clockResponse).isEqualTo(ClockResponse.success(clockedInEmployeeID, TimeEntryType.CLOCK_OUT));
    }

    @Test
    void canClockPauseWithGivenEmployeeID() {
        long clockedInEmployeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithClockedInEmployee(clockedInEmployeeID);

        ClockResponse clockResponse = timeTrackingUseCase.clockPause(clockedInEmployeeID);

        assertThat(clockResponse).isEqualTo(ClockResponse.success(clockedInEmployeeID, TimeEntryType.CLOCK_PAUSE));
    }

    @Test
    void clockingInWhenAlreadyClockedInReturnsError() {
        long clockedInEmployeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithClockedInEmployee(clockedInEmployeeID);

        ClockResponse clockResponse = timeTrackingUseCase.clockIn(clockedInEmployeeID);

        assertThat(clockResponse).isEqualTo(ClockResponse.error(clockedInEmployeeID, ClockError.EMPLOYEE_ALREADY_CLOCKED_IN));
    }

    @Test
    void clockingOutWhenNotClockedInReturnsError() {
        long clockedOutEmployeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithClockedOutEmployee(clockedOutEmployeeID);

        ClockResponse clockResponse = timeTrackingUseCase.clockOut(clockedOutEmployeeID);

        assertThat(clockResponse).isEqualTo(ClockResponse.error(clockedOutEmployeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN));
    }

    @Test
    void clockingPauseWhenNotClockedInReturnsError() {
        long clockedOutEmployeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithClockedOutEmployee(clockedOutEmployeeID);

        ClockResponse clockResponse = timeTrackingUseCase.clockPause(clockedOutEmployeeID);

        assertThat(clockResponse).isEqualTo(ClockResponse.error(clockedOutEmployeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN));
    }

    @Test
    void canClockInAfterClockingOut() throws InterruptedException {
        long employeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithClockedOutEmployee(employeeID);

        timeTrackingUseCase.clockIn(employeeID);
        // wait to ensure that the clock in time is different from the clock out time for comparison
        Thread.sleep(10);

        timeTrackingUseCase.clockOut(employeeID);
        ClockResponse clockResponse = timeTrackingUseCase.clockIn(employeeID);

        assertThat(clockResponse).isEqualTo(ClockResponse.success(employeeID, TimeEntryType.CLOCK_IN));
    }


    @Test
    void successfulClockingSavesTimeEntry() {
        long employeeID = 1L;
        TimeEntryRepository timeEntryRepository = TimeEntryRepositoryStub.withoutEntries();
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithClockedOutEmployee(employeeID, timeEntryRepository);

        assertThat(timeEntryRepository.getTimeEntriesByEmployeeId(employeeID)).isEmpty();
        timeTrackingUseCase.clockIn(employeeID);
        assertThat(timeEntryRepository.getTimeEntriesByEmployeeId(employeeID)).hasSize(1);
        timeTrackingUseCase.clockOut(employeeID);
        assertThat(timeEntryRepository.getTimeEntriesByEmployeeId(employeeID)).hasSize(2);
    }

    @Test
    void unsuccessfulClockingShouldNotSaveTimeEntry() {
        long employeeID = 1L;
        TimeEntryRepository timeEntryRepository = TimeEntryRepositoryStub.withoutEntries();
        TimeTrackingUseCase timeTrackingUseCase = getTimeTrackingServiceWithClockedOutEmployee(employeeID, timeEntryRepository);

        assertThat(timeEntryRepository.getTimeEntriesByEmployeeId(employeeID)).isEmpty();
        timeTrackingUseCase.clockIn(employeeID);
        assertThat(timeEntryRepository.getTimeEntriesByEmployeeId(employeeID)).hasSize(1);
        timeTrackingUseCase.clockIn(employeeID);
        assertThat(timeEntryRepository.getTimeEntriesByEmployeeId(employeeID)).hasSize(1);
    }

    private static TimeTrackingService getTimeTrackingServiceWithClockedOutEmployee(long employeeID) {
        return new TimeTrackingService(new TimeEntryService(TimeEntryRepositoryStub.withoutEntries()),
                                       new EmployeeService(EmployeeRepositoryStub.withEmployee(new Employee(employeeID, ClockState.CLOCKED_OUT)), workScheduleConfig));
    }

    private static TimeTrackingService getTimeTrackingServiceWithClockedOutEmployee(long employeeID, TimeEntryRepository timeEntryRepository) {
        return new TimeTrackingService(new TimeEntryService(timeEntryRepository),
                                       new EmployeeService(EmployeeRepositoryStub.withEmployee(new Employee(employeeID, ClockState.CLOCKED_OUT)), workScheduleConfig));
    }

    private static TimeTrackingService getTimeTrackingServiceWithClockedInEmployee(long employeeID) {
        return new TimeTrackingService(new TimeEntryService(TimeEntryRepositoryStub.withoutEntries()),
                                       new EmployeeService(EmployeeRepositoryStub.withEmployee(new Employee(employeeID, ClockState.CLOCKED_IN)), workScheduleConfig));
    }
}
