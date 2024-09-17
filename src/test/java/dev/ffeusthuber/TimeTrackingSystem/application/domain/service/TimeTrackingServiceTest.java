package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;


import dev.ffeusthuber.TimeTrackingSystem.adapter.out.TimeEntryRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockError;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockResponse;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.TimeTrackingUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.TimeEntryRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeTrackingServiceTest {

    @Test
    void canClockInWithGivenEmployeeID() {
        long employeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub.withoutEntries());

        ClockResponse clockResponse = timeTrackingUseCase.clockIn(employeeID);

        assertThat(clockResponse).isEqualTo(ClockResponse.success(employeeID, TimeEntryType.CLOCK_IN));
    }

    @Test
    void canClockOutWithGivenEmployeeID() {
        long clockedInEmployeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub.withClockedInEmployee(clockedInEmployeeID));

        ClockResponse clockResponse = timeTrackingUseCase.clockOut(clockedInEmployeeID);

        assertThat(clockResponse).isEqualTo(ClockResponse.success(clockedInEmployeeID, TimeEntryType.CLOCK_OUT));
    }

    @Test
    void canClockPauseWithGivenEmployeeID() {
        long clockedInEmployeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub.withClockedInEmployee(clockedInEmployeeID));

        ClockResponse clockResponse = timeTrackingUseCase.clockPause(clockedInEmployeeID);

        assertThat(clockResponse).isEqualTo(ClockResponse.success(clockedInEmployeeID, TimeEntryType.CLOCK_PAUSE));
    }

    @Test
    void clockingInWhenAlreadyClockedInReturnsError() {
        long employeeIDOfClockedInEmployee = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub
                                                                                  .withClockedInEmployee(employeeIDOfClockedInEmployee));

        ClockResponse clockResponse = timeTrackingUseCase.clockIn(employeeIDOfClockedInEmployee);

        assertThat(clockResponse).isEqualTo(ClockResponse.error(employeeIDOfClockedInEmployee, ClockError.EMPLOYEE_ALREADY_CLOCKED_IN));
    }

    @Test
    void clockingOutWhenNotClockedInReturnsError() {
        long employeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub
                                                                                  .withoutEntries());

        ClockResponse clockResponse = timeTrackingUseCase.clockOut(employeeID);

        assertThat(clockResponse).isEqualTo(ClockResponse.error(employeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN));
    }

    @Test
    void clockingPauseWhenNotClockedInReturnsError() {
        long employeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub
                                                                                  .withoutEntries());

        ClockResponse clockResponse = timeTrackingUseCase.clockPause(employeeID);

        assertThat(clockResponse).isEqualTo(ClockResponse.error(employeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN));
    }

    @Test
    void canClockInAfterClockingOut() throws InterruptedException {
        long employeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub.withoutEntries());

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
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(timeEntryRepository);

        assertThat(timeEntryRepository.getTimeEntriesForEmployee(employeeID)).isEmpty();
        timeTrackingUseCase.clockIn(employeeID);
        assertThat(timeEntryRepository.getTimeEntriesForEmployee(employeeID)).hasSize(1);
        timeTrackingUseCase.clockOut(employeeID);
        assertThat(timeEntryRepository.getTimeEntriesForEmployee(employeeID)).hasSize(2);
    }

    @Test
    void unsuccessfulClockingShouldNotSaveTimeEntry() {
        long employeeID = 1L;
        TimeEntryRepository timeEntryRepository = TimeEntryRepositoryStub.withoutEntries();
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(timeEntryRepository);

        assertThat(timeEntryRepository.getTimeEntriesForEmployee(employeeID)).isEmpty();
        timeTrackingUseCase.clockIn(employeeID);
        assertThat(timeEntryRepository.getTimeEntriesForEmployee(employeeID)).hasSize(1);
        timeTrackingUseCase.clockIn(employeeID);
        assertThat(timeEntryRepository.getTimeEntriesForEmployee(employeeID)).hasSize(1);
    }

}
