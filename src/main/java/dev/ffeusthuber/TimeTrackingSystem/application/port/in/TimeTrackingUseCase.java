package dev.ffeusthuber.TimeTrackingSystem.application.port.in;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;

public interface TimeTrackingUseCase {
    TimeEntry clockIn(long employeeID);

    TimeEntry clockOut(long employeeID);

    TimeEntry clockPause(long employeeID);
}
