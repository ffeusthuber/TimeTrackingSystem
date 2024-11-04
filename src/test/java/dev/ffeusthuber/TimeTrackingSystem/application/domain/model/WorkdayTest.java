package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkdayTest {

    private final long EMPLOYEE_ID = 1L;
    private final LocalDate WORK_DATE = LocalDate.of(2021, 1, 1);
    private final ZoneId ZONE_ID = ZoneId.of("UTC");
    private final float SCHEDULED_HOURS = 8.25f;

    @Test
    void whenNoTimeEntryIsAddedToWorkdayThenCalculateWorkedHoursReturnsZero() {
        Workday workday = new Workday(EMPLOYEE_ID, WORK_DATE, ZONE_ID, SCHEDULED_HOURS);
        
        assertThat(workday.calculateWorkedHours()).isEqualTo(0);
    }


    @Test
    void canCalculateWorkedHoursFromClockInAndClockOut() {
        float expectedWorkedHours = 5.5f;
        ZonedDateTime clockInTime = WORK_DATE.atStartOfDay(ZONE_ID);
        ZonedDateTime clockOutTime = clockInTime.plusMinutes((long) (expectedWorkedHours * 60));

        Workday workday = new Workday(EMPLOYEE_ID, WORK_DATE, ZONE_ID, SCHEDULED_HOURS);
        addTimeEntries(workday,
                       new TimeEntry(EMPLOYEE_ID,TimeEntryType.CLOCK_IN,clockInTime),
                       new TimeEntry(EMPLOYEE_ID,TimeEntryType.CLOCK_OUT,clockOutTime));

        assertThat(workday.calculateWorkedHours()).isEqualTo(expectedWorkedHours);
    }

    @Test
    void canCalculateWorkedHoursFromMultipleClockInAndClockOut() {
        float expectedWorkedHours = 5.5f;
        int pauseMinutes = 30;
        ZonedDateTime timeOfFirstEntry = WORK_DATE.atStartOfDay(ZONE_ID);
        Workday workday = new Workday(EMPLOYEE_ID, WORK_DATE, ZONE_ID, SCHEDULED_HOURS);
        addTimeEntries(workday,
                       new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_IN, timeOfFirstEntry),
                       new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_PAUSE, timeOfFirstEntry.plusMinutes((long) ((expectedWorkedHours / 2) * 60))),
                       new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_IN, timeOfFirstEntry.plusMinutes((long) ((expectedWorkedHours / 2) * 60) + pauseMinutes)),
                       new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_OUT, timeOfFirstEntry.plusMinutes((long) ((expectedWorkedHours) * 60) + pauseMinutes))
                      );

        assertThat(workday.calculateWorkedHours()).isEqualTo(expectedWorkedHours);
    }

    @Test
    void ifFirstTimeEntryOfWorkdayIsNotClockInThenCalculateWorkedHoursFromFirstClockIn() {
        float expectedWorkedHours = 5.5f;
        ZonedDateTime timeOfFirstClockIn = WORK_DATE.atStartOfDay(ZONE_ID);
        Workday workday = new Workday(EMPLOYEE_ID, WORK_DATE, ZONE_ID, SCHEDULED_HOURS);
        addTimeEntries(workday,
                       new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_OUT, timeOfFirstClockIn.minusMinutes(60)),
                       new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_IN, timeOfFirstClockIn),
                       new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_OUT, timeOfFirstClockIn.plusMinutes((long) ((expectedWorkedHours) * 60)))
                      );

        assertThat(workday.calculateWorkedHours()).isEqualTo(expectedWorkedHours);
    }

    @Test
    void workdaysSpanningIntoNextDayGetCalculatedCorrectly() {
        float expectedWorkedHours = 2.0f;
        ZonedDateTime timeOfFirstClockIn = ZonedDateTime.of(2021, 1, 1, 23, 0, 0, 0, ZoneOffset.UTC);
        Workday workday = new Workday(EMPLOYEE_ID, WORK_DATE, ZONE_ID, SCHEDULED_HOURS);
        addTimeEntries(workday,
                       new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_IN, timeOfFirstClockIn),
                       new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_OUT, timeOfFirstClockIn.plusMinutes((long)(expectedWorkedHours * 60)))
                      );

        assertThat(workday.calculateWorkedHours()).isEqualTo(expectedWorkedHours);
    }

    private void addTimeEntries(Workday workday, TimeEntry... timEntries){
        for (TimeEntry timeEntry : timEntries) {
            workday.addTimeEntry(timeEntry);
        }
    }
}