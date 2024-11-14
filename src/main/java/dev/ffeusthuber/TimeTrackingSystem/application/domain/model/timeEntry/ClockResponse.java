package dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry;

public record ClockResponse (
        ClockResponseStatus status,
        long employeeId,
        TimeEntryType timeEntryType,
        ClockError clockError)
{
    public ClockResponse(long employeeID, TimeEntryType timeEntryType) {
        this(ClockResponseStatus.SUCCESS, employeeID, timeEntryType, null);
    }

    public ClockResponse(long employeeID, ClockError clockError) {
        this(ClockResponseStatus.ERROR, employeeID, null, clockError);
    }
}
