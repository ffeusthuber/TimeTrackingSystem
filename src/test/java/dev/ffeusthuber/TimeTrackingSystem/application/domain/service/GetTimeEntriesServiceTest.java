package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.TimeEntryRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetTimeEntriesServiceTest {

    @Test
    void returnsAllTimeEntriesForEmployeeAsDto() {
        long employeeID1 = 1L;
        long employeeID2 = 2L;
        ZonedDateTime timeOfFirstEntry = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

        TimeEntry timeEntry1 = new TimeEntry(employeeID1, TimeEntryType.CLOCK_IN, timeOfFirstEntry);
        TimeEntry timeEntry2 = new TimeEntry(employeeID1, TimeEntryType.CLOCK_OUT, timeOfFirstEntry.plusHours(8));
        TimeEntry timeEntry3 = new TimeEntry(employeeID2, TimeEntryType.CLOCK_OUT, timeOfFirstEntry.plusHours(9));
        List<TimeEntry> timeEntries = List.of(timeEntry1,timeEntry2,timeEntry3);
        GetTimeEntriesService getTimeEntriesService = new GetTimeEntriesService(TimeEntryRepositoryStub.withEntries(timeEntries));

        List<TimeEntryDTO> timeEntriesForEmployee1 = getTimeEntriesService.getTimeEntriesForEmployee(employeeID1, ZoneId.of("Europe/Vienna"));

        assertThat(timeEntriesForEmployee1).isEqualTo(List.of(new TimeEntryDTO(timeEntry1, ZoneId.of("Europe/Vienna")), new TimeEntryDTO(timeEntry2, ZoneId.of("Europe/Vienna"))));
    }
}
