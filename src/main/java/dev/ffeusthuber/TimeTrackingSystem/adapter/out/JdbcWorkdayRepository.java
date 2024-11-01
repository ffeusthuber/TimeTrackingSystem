package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Repository
@Primary
public class JdbcWorkdayRepository implements WorkdayRepository {
    @Override
    public Optional<Workday> getWorkdayForEmployeeOnDate(long employeeID, LocalDate date, ZoneId zoneId) {
        return Optional.empty();
    }

    @Override
    public void saveWorkday(Workday workday) {

    }

    @Override
    public Optional<Workday> getLatestWorkdayForEmployee(long employeeID) {
        return Optional.empty();
    }
}
