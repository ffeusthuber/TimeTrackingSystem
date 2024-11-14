package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeEntryDTOTest {

    @Test
    void timeEntryGetsConvertedToDTOCorrectly() {
        TimeEntry timeEntry = new TimeEntry(1, TimeEntryType.CLOCK_IN, ZonedDateTime.of(LocalDateTime.of(2024, 9, 27, 18, 0), ZoneId.of("UTC")));
        TimeEntryDTO timeEntryDTO = new TimeEntryDTO(timeEntry, ZoneId.of("Europe/Vienna"));

        assertThat(timeEntryDTO.weekDay()).isEqualTo("FRIDAY");
        assertThat(timeEntryDTO.date()).isEqualTo("27.SEPTEMBER.2024");
        assertThat(timeEntryDTO.time()).isEqualTo("20:00:00");
        assertThat(timeEntryDTO.timeEntryType()).isEqualTo("Clocked in");
    }
}
