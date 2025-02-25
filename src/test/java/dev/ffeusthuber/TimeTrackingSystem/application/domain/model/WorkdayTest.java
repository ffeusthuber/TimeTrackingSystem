package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkdayTest {

    private final long employeeId = 1L;
    private final LocalDate workDate = LocalDate.of(2021, 1, 1);
    private final ZoneId zoneId = ZoneId.of("UTC");
    private final float scheduledHours = 8.25f;

    @Test
    void whenNoTimeEntryIsAddedToWorkdayThenWorkedAndPausedHoursReturnsZero() {
        Workday workday = new Workday(employeeId, workDate, scheduledHours);
        
        assertThat(workday.calculateWorkedHours()).isEqualTo(0);
        assertThat(workday.calculatePausedHours()).isEqualTo(0);
    }

    @Test
    void canCalculateWorkedHoursFromClockInAndClockOut() {
        float expectedWorkedHours = 5.5f;
        ZonedDateTime clockInTime = workDate.atStartOfDay(zoneId);
        ZonedDateTime clockOutTime = clockInTime.plusMinutes((long) (expectedWorkedHours * 60));

        Workday workday = new Workday(employeeId, workDate, scheduledHours);
        addTimeEntries(workday,
                       new TimeEntry(employeeId, TimeEntryType.CLOCK_IN, clockInTime),
                       new TimeEntry(employeeId, TimeEntryType.CLOCK_OUT, clockOutTime));

        assertThat(workday.calculateWorkedHours()).isEqualTo(expectedWorkedHours);
    }

    @Test
    void canCalculateWorkedHoursFromClockInToNowIfNoClockOutIsPresent() {
        ZonedDateTime clockInTime = workDate.atStartOfDay(zoneId);

        Workday workday = new Workday(employeeId, workDate, scheduledHours);
        addTimeEntries(workday,
                       new TimeEntry(employeeId, TimeEntryType.CLOCK_IN, clockInTime));

        assertThat(workday.calculateWorkedHours()).isEqualTo(workDate.atStartOfDay(zoneId).until(ZonedDateTime.now(zoneId), ChronoUnit.SECONDS) / 3600.0f);
    }

    @Test
    void canCalculateWorkedHoursFromMultipleClockInAndClockOut() {
        float expectedWorkedHours = 5.5f;
        int pauseMinutes = 30;
        ZonedDateTime timeOfFirstEntry = workDate.atStartOfDay(zoneId);
        Workday workday = new Workday(employeeId, workDate, scheduledHours);
        addTimeEntries(workday,
                       new TimeEntry(employeeId, TimeEntryType.CLOCK_IN, timeOfFirstEntry),
                       new TimeEntry(employeeId, TimeEntryType.CLOCK_PAUSE, timeOfFirstEntry.plusMinutes((long) ((expectedWorkedHours / 2) * 60))),
                       new TimeEntry(employeeId, TimeEntryType.CLOCK_IN, timeOfFirstEntry.plusMinutes((long) ((expectedWorkedHours / 2) * 60) + pauseMinutes)),
                       new TimeEntry(employeeId, TimeEntryType.CLOCK_OUT, timeOfFirstEntry.plusMinutes((long) ((expectedWorkedHours) * 60) + pauseMinutes))
                      );

        assertThat(workday.calculateWorkedHours()).isEqualTo(expectedWorkedHours);
    }

    @Test
    void ifFirstTimeEntryOfWorkdayIsNotClockInThenCalculateWorkedHoursFromFirstClockIn() {
        float expectedWorkedHours = 5.5f;
        ZonedDateTime timeOfFirstClockIn = workDate.atStartOfDay(zoneId);
        Workday workday = new Workday(employeeId, workDate, scheduledHours);
        addTimeEntries(workday,
                       new TimeEntry(employeeId, TimeEntryType.CLOCK_OUT, timeOfFirstClockIn.minusMinutes(60)),
                       new TimeEntry(employeeId, TimeEntryType.CLOCK_IN, timeOfFirstClockIn),
                       new TimeEntry(employeeId, TimeEntryType.CLOCK_OUT, timeOfFirstClockIn.plusMinutes((long) ((expectedWorkedHours) * 60)))
                      );

        assertThat(workday.calculateWorkedHours()).isEqualTo(expectedWorkedHours);
    }

    @Test
    void workdaysSpanningIntoNextDayGetCalculatedCorrectly() {
        float expectedWorkedHours = 2.0f;
        ZonedDateTime timeOfFirstClockIn = ZonedDateTime.of(2021, 1, 1, 23, 0, 0, 0, ZoneOffset.UTC);
        Workday workday = new Workday(employeeId, workDate, scheduledHours);
        addTimeEntries(workday,
                       new TimeEntry(employeeId, TimeEntryType.CLOCK_IN, timeOfFirstClockIn),
                       new TimeEntry(employeeId, TimeEntryType.CLOCK_OUT, timeOfFirstClockIn.plusMinutes((long)(expectedWorkedHours * 60)))
                      );

        assertThat(workday.calculateWorkedHours()).isEqualTo(expectedWorkedHours);
    }

    @Test
    void canCalculatePausedHours() {
        float expectedPausedHours = 0.5f;
        ZonedDateTime timeOfPauseStart = workDate.atStartOfDay(zoneId);
        Workday workday = new Workday(employeeId, workDate, scheduledHours);
        addTimeEntries(workday,
                       new TimeEntry(employeeId, TimeEntryType.CLOCK_IN, timeOfPauseStart.minusHours(5)),
                       new TimeEntry(employeeId, TimeEntryType.CLOCK_PAUSE, timeOfPauseStart),
                       new TimeEntry(employeeId, TimeEntryType.CLOCK_IN, timeOfPauseStart.plusMinutes((long) (expectedPausedHours * 60)))
                      );

        assertThat(workday.calculatePausedHours()).isEqualTo(expectedPausedHours);
    }

    private void addTimeEntries(Workday workday, TimeEntry... timEntries){
        for (TimeEntry timeEntry : timEntries) {
            workday.addTimeEntry(timeEntry);
        }
    }
}