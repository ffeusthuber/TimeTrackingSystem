package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;


import dev.ffeusthuber.TimeTrackingSystem.adapter.out.TimeEntryRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockError;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockResponse;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockStatus;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.TimeTrackingUseCase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeTrackingServiceTest {

    @Test
    void canClockInWithGivenEmployeeID() {
        long employeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub.withoutEntries());

        ClockResponse clockResponse = timeTrackingUseCase.clockIn(employeeID);

        assertThat(clockResponse.getStatus()).isEqualTo(ClockStatus.SUCCESS);
        assertThat(clockResponse.getEmployeeID()).isEqualTo(employeeID);
        assertThat(clockResponse.getType()).isEqualTo(TimeEntryType.CLOCK_IN);
    }

    @Test
    void canClockOutWithGivenEmployeeID() {
        long clockedInEmployeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub.withClockedInEmployee(clockedInEmployeeID));

        ClockResponse clockResponse = timeTrackingUseCase.clockOut(clockedInEmployeeID);

        assertThat(clockResponse.getStatus()).isEqualTo(ClockStatus.SUCCESS);
        assertThat(clockResponse.getEmployeeID()).isEqualTo(clockedInEmployeeID);
        assertThat(clockResponse.getType()).isEqualTo(TimeEntryType.CLOCK_OUT);
    }

    @Test
    void canClockPauseWithGivenEmployeeID() {
        long clockedInEmployeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub.withClockedInEmployee(clockedInEmployeeID));

        ClockResponse clockResponse = timeTrackingUseCase.clockPause(clockedInEmployeeID);

        assertThat(clockResponse.getStatus()).isEqualTo(ClockStatus.SUCCESS);
        assertThat(clockResponse.getEmployeeID()).isEqualTo(clockedInEmployeeID);
        assertThat(clockResponse.getType()).isEqualTo(TimeEntryType.CLOCK_PAUSE);
    }

    @Test
    void clockingInWhenAlreadyClockedInReturnsError() {
        long employeeIDOfClockedInEmployee = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub
                                                                                  .withClockedInEmployee(employeeIDOfClockedInEmployee));

        ClockResponse clockResponse = timeTrackingUseCase.clockIn(employeeIDOfClockedInEmployee);

        assertThat(clockResponse.getStatus()).isEqualTo(ClockStatus.ERROR);
        assertThat(clockResponse.getEmployeeID()).isEqualTo(employeeIDOfClockedInEmployee);
        assertThat(clockResponse.getType()).isEqualTo(null);
        assertThat(clockResponse.getError()).isEqualTo(ClockError.EMPLOYEE_ALREADY_CLOCKED_IN);
    }

    @Test
    void clockingOutWhenNotClockedInReturnsError() {
        long employeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub
                                                                                  .withoutEntries());

        ClockResponse clockResponse = timeTrackingUseCase.clockOut(employeeID);

        assertThat(clockResponse.getStatus()).isEqualTo(ClockStatus.ERROR);
        assertThat(clockResponse.getEmployeeID()).isEqualTo(employeeID);
        assertThat(clockResponse.getType()).isEqualTo(null);
        assertThat(clockResponse.getError()).isEqualTo(ClockError.EMPLOYEE_NOT_CLOCKED_IN);
    }

    @Test
    void clockingPauseWhenNotClockedInReturnsError() {
        long employeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub
                                                                                  .withoutEntries());

        ClockResponse clockResponse = timeTrackingUseCase.clockPause(employeeID);

        assertThat(clockResponse.getStatus()).isEqualTo(ClockStatus.ERROR);
        assertThat(clockResponse.getEmployeeID()).isEqualTo(employeeID);
        assertThat(clockResponse.getType()).isEqualTo(null);
        assertThat(clockResponse.getError()).isEqualTo(ClockError.EMPLOYEE_NOT_CLOCKED_IN);
    }

}
