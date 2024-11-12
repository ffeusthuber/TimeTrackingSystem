package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import java.util.List;

public class WeekReport {

    private final int weekNumber;
    private final List<WorkdayDTO> workdays;
    private final float scheduledWorkHoursForWeek;
    private final float workedHoursForWeek;

    public WeekReport(int weekNumber, List<WorkdayDTO> workdays, float scheduledWorkHoursForWeek, float workedHoursForWeek) {
        this.weekNumber = weekNumber;
        this.workdays = workdays;
        this.scheduledWorkHoursForWeek = scheduledWorkHoursForWeek;
        this.workedHoursForWeek = workedHoursForWeek;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public List<WorkdayDTO> getWorkdays() {
        return workdays;
    }

    public float getScheduledWorkHoursForWeek() {
        return roundToOneDecimalPlace(scheduledWorkHoursForWeek);
    }

    public float getWorkedHoursForWeek() {
        return roundToOneDecimalPlace(workedHoursForWeek);
    }

    private float roundToOneDecimalPlace(float value) {
        return Math.round(value * 10) / 10.0f;
    }
}
