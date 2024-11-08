package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

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
import java.util.Optional;

@Repository
@Primary
public class JdbcWorkdayRepository implements WorkdayRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcWorkdayRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Workday> getWorkdayForEmployeeOnDate(long employeeID, LocalDate date, ZoneId zoneId) {
        return Optional.empty();
    }

    @Override
    public void saveWorkday(Workday workday) {
        String sql = "INSERT INTO Workday (employee_id, date, zone_id, hours_scheduled) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, workday.getEmployeeId(), workday.getWorkDate(), workday.getZoneId().toString(), workday.getScheduledHours());
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

    private Workday mapRowToWorkday(ResultSet rs) throws SQLException {
        return new Workday(
                rs.getLong("employee_id"),
                rs.getDate("date").toLocalDate(),
                ZoneId.of(rs.getString("zone_id")),
                rs.getFloat("hours_scheduled")
        );
    }
}
