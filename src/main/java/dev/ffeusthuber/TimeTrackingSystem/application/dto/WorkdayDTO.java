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

    public String getFormatedScheduledWorkTime() {
        int hours = (int) scheduledHours;
        int minutes = (int) ((scheduledHours - hours) * 60);

        return String.format("%d hours %d minutes", hours, minutes);
    }

    public String getFormatedActualWorkTime() {
        int hours = (int) workedHours;
        int minutes = (int) ((workedHours - hours) * 60);
        int seconds = (int) (((workedHours - hours) * 60 - minutes) * 60);

        return String.format("%d hours %d minutes %d seconds", hours, minutes, seconds);
    }

    public String getFormatedDate() {
        return String.format("%02d.%s.%d", date.getDayOfMonth(), date.getMonth().name(), date.getYear());
    }
}
