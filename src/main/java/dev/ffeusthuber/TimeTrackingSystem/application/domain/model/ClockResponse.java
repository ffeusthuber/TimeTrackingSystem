package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

import java.util.Objects;

public class ClockResponse {
    private final ClockStatus clockStatus;
    private final long employeeId;
    private final TimeEntryType timeEntryType;
    private final ClockError clockError;

    public static ClockResponse success(long employeeID, TimeEntryType timeEntryType) {
        return new ClockResponse(ClockStatus.SUCCESS, employeeID, timeEntryType, null);
    }

    public static ClockResponse error(long employeeID, ClockError clockError) {
        return new ClockResponse(ClockStatus.ERROR, employeeID, null, clockError);
    }

    private ClockResponse(ClockStatus clockStatus, long employeeID, TimeEntryType timeEntryType, ClockError clockError) {
        this.clockStatus = clockStatus;
        this.employeeId = employeeID;
        this.timeEntryType = timeEntryType;
        this.clockError = clockError;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClockResponse that)) return false;
        return employeeId == that.employeeId && clockStatus == that.clockStatus && timeEntryType == that.timeEntryType && clockError == that.clockError;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clockStatus, employeeId, timeEntryType, clockError);
    }
}
