package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkScheduleTest {

    @Test
    void givenAWeekdayTheScheduledWorkHoursForThatDayAreReturned(){
        float mondayScheduledWorkHours = 8.5f;
        WorkSchedule workSchedule = WorkSchedule.createSpecificWorkSchedule(mondayScheduledWorkHours, 8f, 8f, 8f, 5.5f, 0f, 0f);

        float scheduledHours = workSchedule.getScheduledWorkHoursForDay(DayOfWeek.MONDAY);

        assertThat(scheduledHours).isEqualTo(mondayScheduledWorkHours);
    }

    @Test
    void workSchedulesAreEqualIfAllScheduledWorkHoursAreEqual(){
        WorkSchedule workSchedule1 = WorkSchedule.createSpecificWorkSchedule(8.5f,8f,8f,8f,5.5f,0f,0f);
        WorkSchedule workSchedule2 = WorkSchedule.createSpecificWorkSchedule(8.5f,8f,8f,8f,5.5f,0f,0f);

        assertThat(workSchedule1).isEqualTo(workSchedule2);
    }

    @Test
    void canGetScheduledWorkHoursForWeek(){
        WorkSchedule workSchedule = WorkSchedule.createSpecificWorkSchedule(8.5f, 8.5f, 8f, 8f, 5.5f, 0f, 0f);

        float scheduledWorkHoursForWeek = workSchedule.getScheduledWorkHoursForWeek();

        assertThat(scheduledWorkHoursForWeek).isEqualTo(38.5f);
    }
}
