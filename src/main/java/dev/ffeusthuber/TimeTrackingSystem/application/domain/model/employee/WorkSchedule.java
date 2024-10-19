package dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Objects;

@PropertySource("classpath:work-schedule.properties")
public class WorkSchedule {
    @Value("${default.work.hours.monday}")
    private static float workHoursMonday;

    @Value("${default.work.hours.tuesday}")
    private static float workHoursTuesday;

    @Value("${default.work.hours.wednesday}")
    private static float workHoursWednesday;

    @Value("${default.work.hours.thursday}")
    private static float workHoursThursday;

    @Value("${default.work.hours.friday}")
    private static float workHoursFriday;

    @Value("${default.work.hours.saturday}")
    private static float workHoursSaturday;

    @Value("${default.work.hours.sunday}")
    private static float workHoursSunday;

    private final float[] scheduledWorkHours;

    private WorkSchedule(float hoursMonday,
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

    public static WorkSchedule createDefaultWorkSchedule() {
        return new WorkSchedule(workHoursMonday, workHoursTuesday, workHoursWednesday, workHoursThursday, workHoursFriday, workHoursSaturday, workHoursSunday);
    }

    public static WorkSchedule createSpecificWorkSchedule(float hoursMonday,
                                                    float hoursTuesday,
                                                    float hoursWednesday,
                                                    float hoursThursday,
                                                    float hoursFriday,
                                                    float hoursSaturday,
                                                    float hoursSunday) {
        return new WorkSchedule(hoursMonday, hoursTuesday, hoursWednesday, hoursThursday, hoursFriday, hoursSaturday, hoursSunday);
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
