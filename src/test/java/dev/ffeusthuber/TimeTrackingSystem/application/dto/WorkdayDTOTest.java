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
        float workedSeconds = 12653;
        float pausedSeconds = 3600;
        ZonedDateTime firstClockInTime = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        ZonedDateTime clockPauseTime = firstClockInTime.plusSeconds((long) workedSeconds / 2);
        ZonedDateTime secondClockInTime = clockPauseTime.plusSeconds((long) pausedSeconds);
        ZonedDateTime clockOutTime = secondClockInTime.plusSeconds((long) workedSeconds / 2);

        Workday workday = new Workday(employeeID, firstClockInTime.toLocalDate(), scheduledHours);
        workday.addTimeEntry(new TimeEntry(employeeID, TimeEntryType.CLOCK_IN, firstClockInTime));
        workday.addTimeEntry(new TimeEntry(employeeID, TimeEntryType.CLOCK_PAUSE, clockPauseTime));
        workday.addTimeEntry(new TimeEntry(employeeID, TimeEntryType.CLOCK_IN, secondClockInTime));
        workday.addTimeEntry(new TimeEntry(employeeID, TimeEntryType.CLOCK_OUT, clockOutTime));

        WorkdayDTO workdayDTO = new WorkdayDTO(workday);

        assertThat(workdayDTO.getFormatedScheduledWorkTime()).isEqualTo("8 hours 30 minutes");
        assertThat(workdayDTO.getFormatedActualWorkTime()).isEqualTo("3 hours 30 minutes 51 seconds");
        assertThat(workdayDTO.getFormatedDate()).isEqualTo("01.JANUARY.2021");
        assertThat(workdayDTO.getFormatedPauseTime()).isEqualTo("60 minutes");
    }

}
