package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

import java.time.DayOfWeek;

public class WorkSchedule {
    private final float[] scheduledWorkHours;

    public WorkSchedule(float hoursMonday,
                        float hoursTuesday,
                        float hoursWednesday,
                        float hoursThursday,
                        float hoursFriday,
                        float hoursSaturday,
                        float hoursSunday) {
        this.scheduledWorkHours = new float[]{hoursMonday, hoursTuesday, hoursWednesday, hoursThursday, hoursFriday, hoursSaturday, hoursSunday};
    }

    public float getScheduledWorkHoursForDay(DayOfWeek dayOfWeek) {
        int index = dayOfWeek.getValue() - 1;
        return scheduledWorkHours[index];
    }
}
