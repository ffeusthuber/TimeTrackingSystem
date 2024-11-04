package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;

public class WorkdayDTO {


    private final float workedHours;
    private final float scheduledHours;

    public WorkdayDTO(Workday workday) {
        this.scheduledHours = workday.getScheduledHours();
        this.workedHours = workday.calculateWorkedHours();
    }

    public float getScheduledHours() {
        return scheduledHours;
    }

    public float getWorkedHours() {
        return workedHours;
    }
}
