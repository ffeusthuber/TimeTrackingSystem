package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.TimeEntryRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeClockStatusServiceTest {

    @Test
    void givenAnEmployeeIdReturnsCorrectTimeEntryTypeForTheGivenEmployee() {
        long clockedInEmployeeId = 1L;
        long clockedOutEmployeeId = 2L;
        long pausedEmployeeId = 3L;
        List<TimeEntry> timeEntries = new ArrayList<>();
        timeEntries.add(new TimeEntry(clockedInEmployeeId, TimeEntryType.CLOCK_IN, null));
        timeEntries.add(new TimeEntry(clockedOutEmployeeId, TimeEntryType.CLOCK_OUT, null));
        timeEntries.add(new TimeEntry(pausedEmployeeId, TimeEntryType.CLOCK_PAUSE, null));

        EmployeeClockStatusService employeeClockStatusService = new EmployeeClockStatusService(TimeEntryRepositoryStub
                                                                                                       .withEntries(timeEntries));

        assertThat(employeeClockStatusService.checkEmployeeClockStatus(clockedInEmployeeId)).isEqualTo(TimeEntryType.CLOCK_IN);
        assertThat(employeeClockStatusService.checkEmployeeClockStatus(clockedOutEmployeeId)).isEqualTo(TimeEntryType.CLOCK_OUT);
        assertThat(employeeClockStatusService.checkEmployeeClockStatus(pausedEmployeeId)).isEqualTo(TimeEntryType.CLOCK_PAUSE);
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
