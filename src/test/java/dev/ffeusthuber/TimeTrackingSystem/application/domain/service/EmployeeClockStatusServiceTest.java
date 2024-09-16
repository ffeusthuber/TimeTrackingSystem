package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.TimeEntryRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeClockStatusServiceTest {

    @Test
    void givenClockedInEmployeeIdReturnsCorrectTimeEntryType() {
        long employeeID = 1L;
        EmployeeClockStatusService employeeClockStatusService = new EmployeeClockStatusService(TimeEntryRepositoryStub
                                                                                                       .withClockedInEmployee(employeeID));

        TimeEntryType timeEntryType = employeeClockStatusService.checkEmployeeClockStatus(employeeID);

        assertThat(timeEntryType).isEqualTo(TimeEntryType.CLOCK_IN);
    }

    @Test
    void forEmployeeWithoutTimeEntryReturnsClockedOutTimeEntryType() {
        long employeeID = 1L;
        EmployeeClockStatusService employeeClockStatusService = new EmployeeClockStatusService(TimeEntryRepositoryStub
                                                                                                       .withoutEntries());

        TimeEntryType timeEntryType = employeeClockStatusService.checkEmployeeClockStatus(employeeID);

        assertThat(timeEntryType).isEqualTo(TimeEntryType.CLOCK_OUT);
    }
}
