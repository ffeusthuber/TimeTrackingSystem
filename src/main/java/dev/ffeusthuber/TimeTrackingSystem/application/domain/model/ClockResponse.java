package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

import java.util.Objects;

public class ClockResponse {
    private final ClockResponseStatus clockResponseStatus;
    private final long employeeId;
    private final TimeEntryType timeEntryType;
    private final ClockError clockError;

    public static ClockResponse success(long employeeID, TimeEntryType timeEntryType) {
        return new ClockResponse(ClockResponseStatus.SUCCESS, employeeID, timeEntryType, null);
    }

    public static ClockResponse error(long employeeID, ClockError clockError) {
        return new ClockResponse(ClockResponseStatus.ERROR, employeeID, null, clockError);
    }

    private ClockResponse(ClockResponseStatus clockResponseStatus, long employeeID, TimeEntryType timeEntryType, ClockError clockError) {
        this.clockResponseStatus = clockResponseStatus;
        this.employeeId = employeeID;
        this.timeEntryType = timeEntryType;
        this.clockError = clockError;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClockResponse that)) return false;
        return employeeId == that.employeeId && clockResponseStatus == that.clockResponseStatus && timeEntryType == that.timeEntryType && clockError == that.clockError;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clockResponseStatus, employeeId, timeEntryType, clockError);
    }
}
