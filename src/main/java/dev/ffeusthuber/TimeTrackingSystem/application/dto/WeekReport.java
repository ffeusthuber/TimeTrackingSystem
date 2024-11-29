package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.WeekOfYear;

import java.util.List;

public record WeekReport(
        WeekOfYear weekOfYear,
        List<WorkdayDTO> workdays,
        float scheduledWorkHoursForWeek,
        float workedHoursForWeek,
        String errorMessage) {

    public WeekReport(WeekOfYear weekOfYear, List<WorkdayDTO> workdays, float scheduledWorkHoursForWeek, float workedHoursForWeek, String errorMessage) {
        this.weekOfYear = weekOfYear;
        this.workdays = workdays;
        this.scheduledWorkHoursForWeek = roundToOneDecimalPlace(scheduledWorkHoursForWeek);
        this.workedHoursForWeek = roundToOneDecimalPlace(workedHoursForWeek);
        this.errorMessage = errorMessage;
    }

    private float roundToOneDecimalPlace(float value) {
        return Math.round(value * 10) / 10.0f;
    }

    public String errorMessage() {
        return errorMessage;
    }
}

