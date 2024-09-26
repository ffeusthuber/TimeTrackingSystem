package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;

public class TimeTrackingSystemControllerTest {

    private final long employeeID = 1L;
    private List<TimeEntry> timeEntries;

    @BeforeEach
    void setUp() {
        TimeEntry timeEntry1 = new TimeEntry(employeeID, TimeEntryType.CLOCK_IN, ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
        TimeEntry timeEntry2 = new  TimeEntry(employeeID, TimeEntryType.CLOCK_OUT, ZonedDateTime.of(2021, 1, 1, 8, 0, 0, 0, ZoneOffset.UTC));
        timeEntries = List.of(timeEntry1, timeEntry2);
    }

    @Test
    void timeEntriesGetDisplayed() {
        fail("Not yet implemented");
    }
}
