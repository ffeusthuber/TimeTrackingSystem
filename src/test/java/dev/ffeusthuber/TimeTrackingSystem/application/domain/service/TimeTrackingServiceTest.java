package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;


import dev.ffeusthuber.TimeTrackingSystem.adapter.out.TimeEntryRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.TimeTrackingUseCase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeTrackingServiceTest {

    @Test
    void canClockInWithGivenEmployeeID() {
        long employeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub.withoutEntries());

        TimeEntry clockInEntry = timeTrackingUseCase.clockIn(employeeID);

        assertThat(clockInEntry.getEmployeeID()).isEqualTo(employeeID);
        assertThat(clockInEntry.getType()).isEqualTo(TimeEntryType.CLOCK_IN);
    }

    @Test
    void canClockOutWithGivenEmployeeID() {
        long employeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub.withoutEntries());

        TimeEntry clockOutEntry = timeTrackingUseCase.clockOut(employeeID);

        assertThat(clockOutEntry.getEmployeeID()).isEqualTo(employeeID);
        assertThat(clockOutEntry.getType()).isEqualTo(TimeEntryType.CLOCK_OUT);
    }

    @Test
    void canClockPauseWithGivenEmployeeID() {
        long employeeID = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub.withoutEntries());

        TimeEntry clockPauseEntry = timeTrackingUseCase.clockPause(employeeID);

        assertThat(clockPauseEntry.getEmployeeID()).isEqualTo(employeeID);
        assertThat(clockPauseEntry.getType()).isEqualTo(TimeEntryType.CLOCK_PAUSE);
    }

    @Test
    void clockingInWhenAlreadyClockedInReturnsNull() {
        long employeeIDOfClockedInEmployee = 1L;
        TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService(TimeEntryRepositoryStub
                                                                                  .withClockedInEmployee(employeeIDOfClockedInEmployee));

        TimeEntry clockInEntry = timeTrackingUseCase.clockIn(employeeIDOfClockedInEmployee);

        assertThat(clockInEntry).isNull();
    }

}
