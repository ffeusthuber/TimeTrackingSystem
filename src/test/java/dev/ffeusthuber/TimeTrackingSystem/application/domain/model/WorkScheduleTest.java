package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkScheduleTest {

    @Test
    void givenAWeekdayTheScheduledWorkHoursForThatDayAreReturned(){
        float mondayScheduledWorkHours = 8.5f;
        WorkSchedule workSchedule = new WorkSchedule(mondayScheduledWorkHours,8f,8f,8f,5.5f,0f,0f);

        float scheduledHours = workSchedule.getScheduledWorkHoursForDay(DayOfWeek.MONDAY);

        assertThat(scheduledHours).isEqualTo(mondayScheduledWorkHours);
    }
}
