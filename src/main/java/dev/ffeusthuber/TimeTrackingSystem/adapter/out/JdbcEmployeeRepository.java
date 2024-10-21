package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.List;


@Repository
@Primary
public class JdbcEmployeeRepository implements EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcEmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Employee employee) {
        String sql = "INSERT INTO Employee (first_name, last_name, email, password, role, clock_state) VALUES (?, ?, ?, ?, ?,?)";
        jdbcTemplate.update(sql,
                            employee.getFirstName(),
                            employee.getLastName(),
                            employee.getEmail(),
                            employee.getPassword(),
                            employee.getRole().name(),
                            employee.getClockState().name());

        String sqlWorkSchedule = "INSERT INTO Work_schedule (employee_id, hours_monday, hours_tuesday, hours_wednesday, hours_thursday, hours_friday, hours_saturday, hours_sunday) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Long employeeId = getEmployeeIDByEmail(employee.getEmail());
        jdbcTemplate.update(sqlWorkSchedule,
                            employeeId,
                            employee.getWorkSchedule().getScheduledWorkHoursForDay(DayOfWeek.MONDAY),
                            employee.getWorkSchedule().getScheduledWorkHoursForDay(DayOfWeek.TUESDAY),
                            employee.getWorkSchedule().getScheduledWorkHoursForDay(DayOfWeek.WEDNESDAY),
                            employee.getWorkSchedule().getScheduledWorkHoursForDay(DayOfWeek.THURSDAY),
                            employee.getWorkSchedule().getScheduledWorkHoursForDay(DayOfWeek.FRIDAY),
                            employee.getWorkSchedule().getScheduledWorkHoursForDay(DayOfWeek.SATURDAY),
                            employee.getWorkSchedule().getScheduledWorkHoursForDay(DayOfWeek.SUNDAY));
    }

    @Override
    public void updateClockState(Long employeeID, String clockState) {
        String sql = "UPDATE Employee SET clock_state = ? WHERE employee_id = ?";
        jdbcTemplate.update(sql, clockState, employeeID);
    }

    @Override
    public List<Employee> getEmployees() {
        String sql = "SELECT e.*, ws.* " +
                "FROM Employee e " +
                "JOIN Work_schedule ws ON e.employee_id = ws.employee_id ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToEmployee(rs));
    }

    @Override
    public Long getEmployeeIDByEmail(String email) {
        try {
            String sql = "SELECT employee_id FROM Employee WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, Long.class, email);
        } catch (EmptyResultDataAccessException e ){
            return null;
        }
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        try {
            String sql = "SELECT e.*, ws.*" +
                    "FROM Employee e " +
                    "JOIN Work_schedule ws ON e.employee_id = ws.employee_id " +
                    "WHERE e.email = ?";
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRowToEmployee(rs), email);
        } catch (EmptyResultDataAccessException e ){
            return null;
        }
    }

    @Override
    public Employee getEmployeeByID(long employeeID) {
        try {
            String sql = "SELECT e.*, ws.*" +
                    "FROM Employee e " +
                    "JOIN Work_schedule ws ON e.employee_id = ws.employee_id " +
                    "WHERE e.employee_id = ?";
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRowToEmployee(rs), employeeID);
        } catch (EmptyResultDataAccessException e ){
            return null;
        }
    }

    private Employee mapRowToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getLong("employee_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("password"),
                EmployeeRole.valueOf(rs.getString("role")),
                ClockState.valueOf(rs.getString("clock_state")),
                mapRowToWorkSchedule(rs)
        );
    }

    private WorkSchedule mapRowToWorkSchedule(ResultSet rs) throws SQLException {
        return WorkSchedule.createSpecificWorkSchedule(
                rs.getFloat("hours_monday"),
                rs.getFloat("hours_tuesday"),
                rs.getFloat("hours_wednesday"),
                rs.getFloat("hours_thursday"),
                rs.getFloat("hours_friday"),
                rs.getFloat("hours_saturday"),
                rs.getFloat("hours_sunday")
                                                      );
    }
}
