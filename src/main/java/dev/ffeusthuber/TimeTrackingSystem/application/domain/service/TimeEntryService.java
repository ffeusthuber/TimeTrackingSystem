package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
public class TimeEntryService {
    public TimeEntry createTimeEntry(long workdayID, TimeEntryType timeEntryType) {
        return new TimeEntry(workdayID, timeEntryType, ZonedDateTime.now(ZoneOffset.UTC));
    }
}
