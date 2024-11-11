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
        float workedSeconds = 3895;
        ZonedDateTime clockInTime = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        ZonedDateTime clockOutTime = clockInTime.plusSeconds((long) workedSeconds);

        Workday workday = new Workday(employeeID, clockInTime.toLocalDate(), scheduledHours);
        workday.addTimeEntry(new TimeEntry(employeeID, TimeEntryType.CLOCK_IN, clockInTime));
        workday.addTimeEntry(new TimeEntry(employeeID, TimeEntryType.CLOCK_OUT, clockOutTime));

        WorkdayDTO workdayDTO = new WorkdayDTO(workday);

        assertThat(workdayDTO.getFormatedScheduledWorkTime()).isEqualTo("8 hours 30 minutes");
        assertThat(workdayDTO.getFormatedActualWorkTime()).isEqualTo("1 hours 4 minutes 55 seconds");
        assertThat(workdayDTO.getFormatedDate()).isEqualTo("01.JANUARY.2021");
    }

}
