package dev.ffeusthuber.TimeTrackingSystem.application.port.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;

import java.time.LocalDate;
import java.util.Optional;

public interface WorkdayRepository {
    void saveWorkday(Workday workday);
    Optional<Workday> getLatestWorkdayForEmployee(long employeeID);
    Optional<Workday> getWorkdayForEmployeeOnDate(long employeeID, LocalDate date);



}
