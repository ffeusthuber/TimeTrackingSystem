package dev.ffeusthuber.TimeTrackingSystem.application.port.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface WorkdayRepository {
    Optional<Workday> getWorkdayForEmployeeOnDate(long employeeID, ZonedDateTime now);
}
