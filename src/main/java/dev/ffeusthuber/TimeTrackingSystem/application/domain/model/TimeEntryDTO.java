package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

public class TimeEntryDTO {

    private final String weekDay;
    private final String date;
    private final String time;
    private final String timeEntryType;

    public TimeEntryDTO(TimeEntry timeEntry, ZoneId zoneId){
        ZonedDateTime zonedDateTime = timeEntry.getEntryDateTime().toInstant().atZone(zoneId);

        this.weekDay = zonedDateTime.getDayOfWeek().name();
        this.date = String.format("%02d.%s.%d", zonedDateTime.getDayOfMonth(), zonedDateTime.getMonth().name(), zonedDateTime.getYear());
        this.time = String.format("%02d:%02d:%02d",zonedDateTime.getHour() ,zonedDateTime.getMinute() ,zonedDateTime.getSecond());
        this.timeEntryType = switch (timeEntry.getType()) {
            case CLOCK_IN -> "Clocked in";
            case CLOCK_OUT -> "Clocked out";
            case CLOCK_PAUSE -> "Clocked pause";
        };
    }

    public String getWeekday() {
        return this.weekDay;
    }

    public String getDate() {
        return this.date;
    }

    public String getTime() {
        return this.time;
    }

    public String getTimeEntryType() {
        return this.timeEntryType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeEntryDTO that)) return false;
        return Objects.equals(weekDay, that.weekDay) && Objects.equals(date, that.date) && Objects.equals(time, that.time) && Objects.equals(timeEntryType, that.timeEntryType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weekDay, date, time, timeEntryType);
    }
}
