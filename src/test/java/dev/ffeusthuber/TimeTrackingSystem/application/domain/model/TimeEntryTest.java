package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeEntryTest {

    @Test
    void entryDateTimeGetsCorrectlySetWhenTimeEntryIsCreated() {
        long employeeID = 1L;
        TimeEntryType entryType = TimeEntryType.CLOCK_IN;
        ZonedDateTime entryDateTime = ZonedDateTime.now(ZoneOffset.UTC);

        TimeEntry timeEntry = new TimeEntry(employeeID, entryType, entryDateTime);

        assertThat(timeEntry.getEntryDateTime()).isEqualTo(entryDateTime);
    }

    @Test
    void timeEntryComparisonBasedOnEntryDateTime() {
        long employeeID = 1L;
        TimeEntryType entryType = TimeEntryType.CLOCK_IN;
        ZonedDateTime entryDateTime = ZonedDateTime.now(ZoneOffset.UTC);

        TimeEntry timeEntry1 = new TimeEntry(employeeID, entryType, entryDateTime);
        TimeEntry timeEntry2 = new TimeEntry(employeeID, entryType, entryDateTime.plusMinutes(1));

        assertThat(timeEntry1).isLessThan(timeEntry2);
    }
}
