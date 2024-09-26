package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.TimeEntryRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;

@Repository
@Primary
public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TimeEntry> getTimeEntriesByEmployeeId(long employeeID) {
        String sql = "SELECT * FROM Time_entry WHERE employee_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToTimeEntry(rs), employeeID);
    }

private TimeEntry mapRowToTimeEntry(ResultSet rs) throws SQLException {
        return new TimeEntry(
                rs.getLong("employee_id"),
                TimeEntryType.valueOf(rs.getString("entry_type")),
                rs.getTimestamp("entry_date_time").toInstant().atZone(ZoneId.of("UTC"))
        );
    }

    @Override
    public void save(TimeEntry timeEntry) {
        String sql = "INSERT INTO Time_entry (employee_id, entry_type, entry_date_time) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, timeEntry.getEmployeeID(), timeEntry.getType().name(), timeEntry.getEntryDateTime());
    }
}
