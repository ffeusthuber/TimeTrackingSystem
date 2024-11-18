package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;

import java.time.DayOfWeek;

public record WorkScheduleDTO(
        float hoursMonday,
        float hoursTuesday,
        float hoursWednesday,
        float hoursThursday,
        float hoursFriday,
        float hoursSaturday,
        float hoursSunday) {

    public WorkScheduleDTO(WorkSchedule workSchedule) {
        this(
                workSchedule.getScheduledWorkHoursForDay(DayOfWeek.MONDAY),
                workSchedule.getScheduledWorkHoursForDay(DayOfWeek.TUESDAY),
                workSchedule.getScheduledWorkHoursForDay(DayOfWeek.WEDNESDAY),
                workSchedule.getScheduledWorkHoursForDay(DayOfWeek.THURSDAY),
                workSchedule.getScheduledWorkHoursForDay(DayOfWeek.FRIDAY),
                workSchedule.getScheduledWorkHoursForDay(DayOfWeek.SATURDAY),
                workSchedule.getScheduledWorkHoursForDay(DayOfWeek.SUNDAY)
            );
    }

    public WorkSchedule toWorkSchedule() {
        return WorkSchedule.createSpecificWorkSchedule(
                hoursMonday,
                hoursTuesday,
                hoursWednesday,
                hoursThursday,
                hoursFriday,
                hoursSaturday,
                hoursSunday);
    }
}