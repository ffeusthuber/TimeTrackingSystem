package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkdayDTOTest {

    @Test
    void workdayGetsConvertedToDTOCorrectly() {
        long employeeID = 1L;
        float scheduledHours = 8.5f;
        float workedHours = 8.0f;
        ZonedDateTime clockInTime = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        ZonedDateTime clockOutTime = clockInTime.plusHours((long) workedHours);

        Workday workday = new Workday(employeeID, clockInTime.toLocalDate(), clockInTime.getZone(), scheduledHours);
        workday.addTimeEntry(new TimeEntry(employeeID, TimeEntryType.CLOCK_IN, clockInTime));
        workday.addTimeEntry(new TimeEntry(employeeID, TimeEntryType.CLOCK_OUT, clockOutTime));

        WorkdayDTO workdayDTO = new WorkdayDTO(workday);

        assertThat(workdayDTO.getScheduledHours()).isEqualTo(scheduledHours);
        assertThat(workdayDTO.getWorkedHours()).isEqualTo(workedHours);
    }

}
