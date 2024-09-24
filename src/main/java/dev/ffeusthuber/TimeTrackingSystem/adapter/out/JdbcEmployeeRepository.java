package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
@Primary
public class JdbcEmployeeRepository implements EmployeeRepository {

    JdbcTemplate jdbcTemplate;

    public JdbcEmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Employee employee) {
        String sql = "INSERT INTO Employee (first_name, last_name, email, password, role, clock_state) VALUES (?, ?, ?, ?, ?,?)";
        jdbcTemplate.update(sql, employee.getFirstName(), employee.getLastName(), employee.getEmail(), employee.getPassword(), employee.getRole().name(), employee.getClockState().name());
    }

    @Override
    public List<Employee> getEmployees() {
        String sql = "SELECT * FROM Employee";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToEmployee(rs));
    }

    @Override
    public Long getEmployeeIdByEmail(String email) {
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
            String sql = "SELECT * FROM Employee WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRowToEmployee(rs), email);
        } catch (EmptyResultDataAccessException e ){
            return null;
        }
    }

    @Override
    public Employee getEmployeeById(long employeeID) {
        try {
            String sql = "SELECT * FROM Employee WHERE employee_id = ?";
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
                ClockState.valueOf(rs.getString("clock_state"))
        );
    }
}
