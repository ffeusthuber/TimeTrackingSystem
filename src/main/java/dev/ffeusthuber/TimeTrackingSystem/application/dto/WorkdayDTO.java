package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;

import java.time.LocalDate;

public class WorkdayDTO {
    private final float workedHours;
    private final float pausedHours;
    private final float scheduledHours;
    private final LocalDate date;

    public WorkdayDTO(Workday workday) {
        this.workedHours = workday.calculateWorkedHours();
        this.pausedHours = workday.calculatePausedHours();
        this.scheduledHours = workday.getScheduledHours();
        this.date = workday.getWorkDate();
    }

    public String getFormatedActualWorkTime() {
        return formatToHoursMinutesAndSeconds(workedHours);
    }

    public String getFormatedPauseTime() {
        return formatToMinutes(pausedHours);
    }

    public String getFormatedScheduledWorkTime() {
        return formatedToHoursAndMinutes(scheduledHours);
    }

    public String getFormatedDate() {
        return String.format("%02d.%s.%d", date.getDayOfMonth(), date.getMonth().name(), date.getYear());
    }

    private String formatToMinutes(float hours) {
        int minutes = (int) (hours * 60);
        return String.format("%d minutes", minutes);
    }

    private String formatedToHoursAndMinutes(float hours) {
        int fullHours = (int) hours;
        int minutes = (int) ((hours - fullHours) * 60);

        return String.format("%d hours %d minutes", fullHours, minutes);
    }

    private String formatToHoursMinutesAndSeconds(float hours) {
        int fullHours = (int) hours;
        int minutes = (int) ((hours - fullHours) * 60);
        int seconds = (int) (((hours - fullHours) * 60 - minutes) * 60);

        return String.format("%d hours %d minutes %d seconds", fullHours, minutes, seconds);
    }
}
