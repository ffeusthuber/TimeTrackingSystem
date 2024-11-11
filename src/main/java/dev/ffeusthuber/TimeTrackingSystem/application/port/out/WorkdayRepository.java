package dev.ffeusthuber.TimeTrackingSystem.application.port.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkdayRepository {
    Workday saveWorkday(Workday workday);
    Optional<Workday> getLatestWorkdayForEmployee(long employeeID);
    Optional<Workday> getWorkdayForEmployeeOnDate(long employeeID, LocalDate date);
    List<Workday> getWorkdaysForEmployeeBetweenDates(long employeeId, LocalDate fromIncluding, LocalDate toIncluding);
}
