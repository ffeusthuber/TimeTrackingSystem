package dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkSchedule that)) return false;
        return Objects.deepEquals(scheduledWorkHours, that.scheduledWorkHours);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(scheduledWorkHours);
    }
}
