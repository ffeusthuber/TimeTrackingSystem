package dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee;

import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

public class WorkSchedule {
    private static float defaultWorkHoursMonday;
    private static float defaultWorkHoursTuesday;
    private static float defaultWorkHoursWednesday;
    private static float defaultWorkHoursThursday;
    private static float defaultWorkHoursFriday;
    private static float defaultWorkHoursSaturday;
    private static float defaultWorkHoursSunday;

    static {
        Properties properties = new Properties();
        try (InputStream input = WorkSchedule.class.getClassLoader().getResourceAsStream("work-schedule.properties")) {
            if (input == null) {
                throw new IOException("Unable to find work-schedule.properties");
            }
            properties.load(input);
            defaultWorkHoursMonday = Float.parseFloat(properties.getProperty("default.work.hours.monday", "0"));
            defaultWorkHoursTuesday = Float.parseFloat(properties.getProperty("default.work.hours.tuesday", "0"));
            defaultWorkHoursWednesday = Float.parseFloat(properties.getProperty("default.work.hours.wednesday", "0"));
            defaultWorkHoursThursday = Float.parseFloat(properties.getProperty("default.work.hours.thursday", "0"));
            defaultWorkHoursFriday = Float.parseFloat(properties.getProperty("default.work.hours.friday", "0"));
            defaultWorkHoursSaturday = Float.parseFloat(properties.getProperty("default.work.hours.saturday", "0"));
            defaultWorkHoursSunday = Float.parseFloat(properties.getProperty("default.work.hours.sunday", "0"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

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

    public float getScheduledWorkHoursForWeek() {
        float total = 0;
        for (float hours : scheduledWorkHours) {
            total += hours;
        }
        return total;
    }

    public static WorkSchedule createDefaultWorkSchedule() {
        return new WorkSchedule(defaultWorkHoursMonday, defaultWorkHoursTuesday, defaultWorkHoursWednesday, defaultWorkHoursThursday, defaultWorkHoursFriday, defaultWorkHoursSaturday, defaultWorkHoursSunday);
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
