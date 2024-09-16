package dev.ffeusthuber.TimeTrackingSystem.application.domain.model;

public class ClockResponse {
    private final ClockStatus clockStatus;
    private final long employeeId;
    private final TimeEntryType timeEntryType;
    private final ClockError clockError;

    private ClockResponse(ClockStatus clockStatus, long employeeID, TimeEntryType timeEntryType, ClockError clockError) {
        this.clockStatus = clockStatus;
        this.employeeId = employeeID;
        this.timeEntryType = timeEntryType;
        this.clockError = clockError;
    }
    public ClockStatus getStatus() {
        return this.clockStatus;
    }

    public long getEmployeeID() {
        return this.employeeId;
    }

    public TimeEntryType getType() {
        return this.timeEntryType;
    }

    public ClockError getError() {
        return this.clockError;
    }

    public static ClockResponse success(long employeeID, TimeEntryType timeEntryType) {
        return new ClockResponse(ClockStatus.SUCCESS, employeeID, timeEntryType, null);
    }

    public static ClockResponse error(long employeeID, ClockError clockError) {
        return new ClockResponse(ClockStatus.ERROR, employeeID, null, clockError);
    }

}
