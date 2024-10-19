package dev.ffeusthuber.TimeTrackingSystem.config;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:work-schedule.properties")
public class WorkScheduleConfig {

    @Value("${default.work.hours.monday}")
    private float workHoursMonday;

    @Value("${default.work.hours.tuesday}")
    private float workHoursTuesday;

    @Value("${default.work.hours.wednesday}")
    private float workHoursWednesday;

    @Value("${default.work.hours.thursday}")
    private float workHoursThursday;

    @Value("${default.work.hours.friday}")
    private float workHoursFriday;

    @Value("${default.work.hours.saturday}")
    private float workHoursSaturday;

    @Value("${default.work.hours.sunday}")
    private float workHoursSunday;

    public WorkSchedule defaultWorkSchedule() {
        return new WorkSchedule(workHoursMonday, workHoursTuesday, workHoursWednesday, workHoursThursday, workHoursFriday, workHoursSaturday, workHoursSunday);
    }
}
