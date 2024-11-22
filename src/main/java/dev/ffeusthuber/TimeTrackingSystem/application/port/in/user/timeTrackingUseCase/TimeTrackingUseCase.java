package dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.timeTrackingUseCase;

public interface TimeTrackingUseCase {
    ClockResponse clockIn(long employeeID);

    ClockResponse clockOut(long employeeID);

    ClockResponse clockPause(long employeeID);
}
