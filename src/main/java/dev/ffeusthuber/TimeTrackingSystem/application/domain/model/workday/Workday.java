package dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday;

import java.time.ZoneId;

public class Workday {
    private final long employeeId;
    private final ZoneId timeZoneId;

    public Workday(long employeeId, ZoneId timeZoneId) {
        this.employeeId = employeeId;
        this.timeZoneId = timeZoneId;
    }

    public ZoneId getTimeZoneId() {
        return this.timeZoneId;
    }

    public long getEmployeeId() {
        return this.employeeId;
    }
}
