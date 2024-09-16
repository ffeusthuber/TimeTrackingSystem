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

        ClockResponse clockInResponse = timeTrackingUseCase.clockIn(employeeID);

        assertThat(clockInResponse.getStatus()).isEqualTo(ClockStatus.SUCCESS);
        assertThat(clockInResponse.getEmployeeID()).isEqualTo(employeeID);
        assertThat(clockInResponse.getType()).isEqualTo(TimeEntryType.CLOCK_IN);
    }

    @Test
    void canClockOutWithGivenEmployeeID() {
        long employeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub.withoutEntries());

        ClockResponse clockOutResponse = timeTrackingUseCase.clockOut(employeeID);

        assertThat(clockOutResponse.getStatus()).isEqualTo(ClockStatus.SUCCESS);
        assertThat(clockOutResponse.getEmployeeID()).isEqualTo(employeeID);
        assertThat(clockOutResponse.getType()).isEqualTo(TimeEntryType.CLOCK_OUT);
    }

    @Test
    void canClockPauseWithGivenEmployeeID() {
        long employeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub.withoutEntries());

        ClockResponse clockPauseResponse = timeTrackingUseCase.clockPause(employeeID);

        assertThat(clockPauseResponse.getStatus()).isEqualTo(ClockStatus.SUCCESS);
        assertThat(clockPauseResponse.getEmployeeID()).isEqualTo(employeeID);
        assertThat(clockPauseResponse.getType()).isEqualTo(TimeEntryType.CLOCK_PAUSE);
    }

    @Test
    void clockingInWhenAlreadyClockedInReturnsError() {
        long employeeIDOfClockedInEmployee = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub
                                                                                  .withClockedInEmployee(employeeIDOfClockedInEmployee));

        ClockResponse clockPauseResponse = timeTrackingUseCase.clockIn(employeeIDOfClockedInEmployee);

        assertThat(clockPauseResponse.getStatus()).isEqualTo(ClockStatus.ERROR);
        assertThat(clockPauseResponse.getEmployeeID()).isEqualTo(employeeIDOfClockedInEmployee);
        assertThat(clockPauseResponse.getType()).isEqualTo(null);
        assertThat(clockPauseResponse.getError()).isEqualTo(ClockError.ALREADY_CLOCKED_IN);
    }

}
