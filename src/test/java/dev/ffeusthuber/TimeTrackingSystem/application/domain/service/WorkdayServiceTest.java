package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Workday;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkdayServiceTest {

    @Test
    void canCreateNewWorkdayForEmployeeFromClockInTimeEntry() {
        WorkdayService workdayService = new WorkdayService();
        long employeeId = 1L;
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        TimeEntry timeEntry = new TimeEntry(employeeId, TimeEntryType.CLOCK_IN, zonedDateTime);

        Workday workday = workdayService.createNewWorkdayForEmployee(timeEntry);

        assertThat(workday.getTimeZoneId()).isEqualTo(zonedDateTime.getZone());
        assertThat(workday.getEmployeeId()).isEqualTo(employeeId);
    }

}
