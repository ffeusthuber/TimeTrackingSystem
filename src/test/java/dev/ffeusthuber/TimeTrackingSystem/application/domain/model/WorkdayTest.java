package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkdayTest {

    private final long EMPLOYEE_ID = 1L;
    private final ZoneId ZONE_ID = ZoneId.of("UTC");

    @Test
    void whenNoTimeEntryIsAddedToWorkdayThenCalculateWorkedHoursReturnsZero() {
        Workday workday = new Workday(EMPLOYEE_ID, ZONE_ID);

        double workedHours = workday.calculateWorkedHours();

        assertThat(workedHours).isEqualTo(0);
    }

    @Test
    void canCalculateWorkedHoursFromClockInAndClockOut() {
        double expectedWorkedHours = 5.5;
        ZonedDateTime timeOfFirstClockIn = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        TimeEntry timeEntry1 = new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_IN, timeOfFirstClockIn);
        TimeEntry timeEntry2 = new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_OUT, timeOfFirstClockIn.plusMinutes((long)(expectedWorkedHours * 60)));
        Workday workday = new Workday(EMPLOYEE_ID, ZONE_ID);
        addTimeEntries(workday, timeEntry1, timeEntry2);

        double workedHours = workday.calculateWorkedHours();

        assertThat(workedHours).isEqualTo(expectedWorkedHours);
    }

    @Test
    void canCalculateWorkedHoursFromMultipleClockInAndClockOut() {
        double expectedWorkedHours = 5.5;
        int pauseMinutes = 30;
        ZonedDateTime timeOfFirstEntry = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        TimeEntry timeEntry1 = new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_IN, timeOfFirstEntry);
        TimeEntry timeEntry2 = new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_PAUSE, timeOfFirstEntry.plusMinutes((long) ((expectedWorkedHours / 2) * 60)));
        TimeEntry timeEntry3 = new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_IN, timeOfFirstEntry.plusMinutes((long) ((expectedWorkedHours / 2) * 60) + pauseMinutes));
        TimeEntry timeEntry4 = new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_OUT, timeOfFirstEntry.plusMinutes((long) ((expectedWorkedHours) * 60) + pauseMinutes));
        Workday workday = new Workday(EMPLOYEE_ID, ZONE_ID);
        addTimeEntries(workday, timeEntry1, timeEntry2, timeEntry3, timeEntry4);

        double workedHours = workday.calculateWorkedHours();

        assertThat(workedHours).isEqualTo(expectedWorkedHours);
    }

    @Test
    void ifFirstTimeEntryOfWorkdayIsNotClockInThenCalculateWorkedHoursFromFirstClockIn() {
        double expectedWorkedHours = 5.5;
        ZonedDateTime timeOfFirstClockIn = ZonedDateTime.of(2021, 1, 1, 8, 0, 0, 0, ZoneOffset.UTC);
        TimeEntry timeEntry1 = new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_OUT, timeOfFirstClockIn.minusMinutes(60));
        TimeEntry timeEntry2 = new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_IN, timeOfFirstClockIn);
        TimeEntry timeEntry3 = new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_IN, timeOfFirstClockIn.plusMinutes((long)(expectedWorkedHours * 60)));
        Workday workday = new Workday(EMPLOYEE_ID, ZONE_ID);
        addTimeEntries(workday, timeEntry1, timeEntry2, timeEntry3);

        double workedHours = workday.calculateWorkedHours();

        assertThat(workedHours).isEqualTo(expectedWorkedHours);
    }

    private void addTimeEntries(Workday workday, TimeEntry... timEntries){
        for (TimeEntry timeEntry : timEntries) {
            workday.addTimeEntry(timeEntry);
        }
    }
}