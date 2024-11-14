package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;

import java.time.ZoneId;

public record TimeEntryDTO(
        String weekDay,
        String date,
        String time,
        String timeEntryType) {

    public TimeEntryDTO(TimeEntry timeEntry, ZoneId zoneId) {
        this(
                timeEntry.entryDateTime().toInstant().atZone(zoneId).getDayOfWeek().name(),
                String.format("%02d.%s.%d",
                              timeEntry.entryDateTime().toInstant().atZone(zoneId).getDayOfMonth(),
                              timeEntry.entryDateTime().toInstant().atZone(zoneId).getMonth().name(),
                              timeEntry.entryDateTime().toInstant().atZone(zoneId).getYear()),
                String.format("%02d:%02d:%02d",
                              timeEntry.entryDateTime().toInstant().atZone(zoneId).getHour(),
                              timeEntry.entryDateTime().toInstant().atZone(zoneId).getMinute(),
                              timeEntry.entryDateTime().toInstant().atZone(zoneId).getSecond()),
                switch (timeEntry.entryType()) {
                    case CLOCK_IN -> "Clocked in";
                    case CLOCK_OUT -> "Clocked out";
                    case CLOCK_PAUSE -> "Clocked pause";
                }
            );
    }
}
