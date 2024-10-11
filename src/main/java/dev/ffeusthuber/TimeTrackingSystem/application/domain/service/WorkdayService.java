package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Workday;

import java.time.ZoneId;

public class WorkdayService {

    public Workday createNewWorkdayForEmployee(TimeEntry timeEntry) {
        long employeeId = timeEntry.getEmployeeID();

        ZoneId zoneId = timeEntry.getEntryDateTime().getZone();
        return new Workday(employeeId, zoneId);
    }
}
