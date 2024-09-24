package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Repository
@Primary
public class JdbcEmployeeRepository implements EmployeeRepository {

    JdbcTemplate jdbcTemplate;

    public JdbcEmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Employee getEmployeeById(long employeeID) {
        return null;
    }

    @Override
    public void save(Employee employee) {
        String sql = "INSERT INTO Employee (first_name, last_name, email, password, role, clock_state) VALUES (?, ?, ?, ?, ?,?)";
        jdbcTemplate.update(sql, employee.getFirstName(), employee.getLastName(), employee.getEmail(), employee.getPassword(), employee.getRole().name(), employee.getClockState().name());
    }

    @Override
    public List<Employee> getEmployees() {
        String sql = "SELECT * FROM Employee";
        return jdbcTemplate.queryForList(sql).stream()
                           .map(row -> new Employee(
                                   ((Number) row.get("employee_id")).longValue(),
                                   (String) row.get("first_name"),
                                   (String) row.get("last_name"),
                                   (String) row.get("email"),
                                   (String) row.get("password"),
                                   EmployeeRole.valueOf((String) row.get("role")),
                                   ClockState.valueOf((String) row.get("clock_state"))
                           ))
                           .collect(Collectors.toList());
    }

    @Override
    public Long getEmployeeIdByEmail(String email) {
        return 0L;
    }
}
