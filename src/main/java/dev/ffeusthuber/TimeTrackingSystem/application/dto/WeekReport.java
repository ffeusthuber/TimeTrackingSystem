package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import java.util.List;

public record WeekReport(
        int weekNumber,
        List<WorkdayDTO> workdays,
        float scheduledWorkHoursForWeek,
        float workedHoursForWeek) {

    public WeekReport(int weekNumber, List<WorkdayDTO> workdays, float scheduledWorkHoursForWeek, float workedHoursForWeek) {
        this.weekNumber = weekNumber;
        this.workdays = workdays;
        this.scheduledWorkHoursForWeek = roundToOneDecimalPlace(scheduledWorkHoursForWeek);
        this.workedHoursForWeek = roundToOneDecimalPlace(workedHoursForWeek);
    }

    private float roundToOneDecimalPlace(float value) {
        return Math.round(value * 10) / 10.0f;
    }
}

