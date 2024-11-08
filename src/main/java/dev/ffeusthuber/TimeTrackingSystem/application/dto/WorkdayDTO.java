package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;

import java.time.LocalDate;

public class WorkdayDTO {


    private final float workedHours;
    private final float scheduledHours;
    private final LocalDate date;

    public WorkdayDTO(Workday workday) {
        this.scheduledHours = workday.getScheduledHours();
        this.workedHours = workday.calculateWorkedHours();
        this.date = workday.getWorkDate();
    }

    public float getScheduledHours() {
        return scheduledHours;
    }

    public float getWorkedHours() {
        return workedHours;
    }

    public String getFormatedDate() {
        return String.format("%02d.%s.%d", date.getDayOfMonth(), date.getMonth().name(), date.getYear());
    }
}
