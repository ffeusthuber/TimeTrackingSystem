package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.WeekOfYear;

import java.util.List;

public record WeekReport(
        WeekOfYear weekOfYear,
        List<WorkdayDTO> workdays,
        float scheduledWorkHoursForWeek,
        float workedHoursForWeek) {

    public WeekReport(WeekOfYear weekOfYear, List<WorkdayDTO> workdays, float scheduledWorkHoursForWeek, float workedHoursForWeek) {
        this.weekOfYear = weekOfYear;
        this.workdays = workdays;
        this.scheduledWorkHoursForWeek = roundToOneDecimalPlace(scheduledWorkHoursForWeek);
        this.workedHoursForWeek = roundToOneDecimalPlace(workedHoursForWeek);
    }

    private float roundToOneDecimalPlace(float value) {
        return Math.round(value * 10) / 10.0f;
    }
}

