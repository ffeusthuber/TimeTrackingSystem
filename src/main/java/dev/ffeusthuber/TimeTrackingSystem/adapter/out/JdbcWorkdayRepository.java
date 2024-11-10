package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class JdbcWorkdayRepository implements WorkdayRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcWorkdayRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Workday> getWorkdayForEmployeeOnDate(long employeeID, LocalDate date) {
        try {
            String sql = "SELECT * FROM Workday WHERE employee_id = ? AND date = ?";
            Workday workday = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRowToWorkday(rs), employeeID, date);
            return Optional.ofNullable(workday);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Workday saveWorkday(Workday workday) {
        String sql = "INSERT INTO Workday (employee_id, date, hours_scheduled) SELECT ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM Workday WHERE employee_id = ? AND date = ?)";
        jdbcTemplate.update(sql, workday.getEmployeeId(), workday.getWorkDate(), workday.getScheduledHours(), workday.getEmployeeId(), workday.getWorkDate());

        Workday workdayWithId = getWorkdayForEmployeeOnDate(workday.getEmployeeId(), workday.getWorkDate())
                .orElseThrow(() -> new IllegalStateException("Failed to retrieve saved workday"));
        long workdayId = workdayWithId.getWorkdayId();

        List<TimeEntry> timeEntries = workday.getTimeEntries();
        timeEntries.forEach(timeEntry -> {
            String timeEntrySql = "INSERT INTO Time_entry (workday_id, entry_type, entry_date_time) SELECT ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM Time_entry WHERE workday_id = ? AND entry_type = ? AND entry_date_time = ?)";
            jdbcTemplate.update(timeEntrySql, workdayId, timeEntry.getType().name(), timeEntry.getEntryDateTime(), workdayId, timeEntry.getType().name(), timeEntry.getEntryDateTime());
        });

        return workdayWithId;
    }


    @Override
    public Optional<Workday> getLatestWorkdayForEmployee(long employeeID) {
        try {
            String sql = "SELECT * FROM Workday WHERE employee_id = ? ORDER BY date DESC LIMIT 1";
            Workday workday = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRowToWorkday(rs), employeeID);
            return Optional.ofNullable(workday);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private TimeEntry mapRowToTimeEntry(ResultSet rs) throws SQLException {
        return new TimeEntry(
                rs.getLong("workday_id"),
                TimeEntryType.valueOf(rs.getString("entry_type")),
                rs.getTimestamp("entry_date_time").toInstant().atZone(ZoneId.of("UTC"))
        );
    }

    private List<TimeEntry> getTimeEntriesForWorkday(long workdayId) {
        String sql = "SELECT * FROM Time_entry WHERE workday_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToTimeEntry(rs), workdayId);
    }

    private Workday mapRowToWorkday(ResultSet rs) throws SQLException {
        return new Workday(
                rs.getLong("workday_id"),
                rs.getLong("employee_id"),
                rs.getDate("date").toLocalDate(),
                rs.getFloat("hours_scheduled"),
                getTimeEntriesForWorkday(rs.getLong("workday_id"))
        );
    }
}
