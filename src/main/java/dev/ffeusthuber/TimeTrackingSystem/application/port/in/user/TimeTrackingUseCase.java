package dev.ffeusthuber.TimeTrackingSystem.application.port.in.user;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockResponse;

public interface TimeTrackingUseCase {
    ClockResponse clockIn(long employeeID);

    ClockResponse clockOut(long employeeID);

    ClockResponse clockPause(long employeeID);
}
