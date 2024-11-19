package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeService;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Tag("io")
public class JdbcEmployeeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    private final String email = "j.doe@test-mail.com";

    @BeforeEach
    void setUp() {
        EmployeeService employeeService = new EmployeeService(employeeRepository);
    }

    @AfterEach
    void clearDatabase() {
        jdbcTemplate.execute("DELETE FROM Work_schedule");
        jdbcTemplate.execute("DELETE FROM Employee");
    }

    @Test
    void employeeCanBeSaved() {
        saveNewEmployee();

        assertThat(employeeRepository.getAllEmployees()).hasSize(1);
    }

    @Test
    void canGetEmployeeIdFromEmail(){
        saveNewEmployee();

        Long employeeId = employeeRepository.getEmployeeIDByEmail(email);

        assertThat(employeeId).isNotNull();
    }

    @Test
    void canGetEmployeeFromEmail(){
        saveNewEmployee();

        Employee employee = employeeRepository.getEmployeeByEmail(email);

        assertThat(employee).isNotNull();
    }

    @Test
    void emptySearchResultsGetHandledCorrectly(){
        String nonExistingEmail = "not existing email";
        long nonExistingEmployeeId = -1L;

        assertThat(employeeRepository.getEmployeeByEmail(nonExistingEmail)).isNull();
        assertThat(employeeRepository.getEmployeeByID(nonExistingEmployeeId)).isNull();
        assertThat(employeeRepository.getAllEmployees()).isEmpty();
        assertThat(employeeRepository.getEmployeeIDByEmail(nonExistingEmail)).isNull();
    }

    @Test
    void canGetEmployeeByID() {
        saveNewEmployee();
        Employee expectedEmployee = employeeRepository.getEmployeeByEmail(email);
        Long employeeId = employeeRepository.getEmployeeIDByEmail(email);

        Employee actualEmployee = employeeRepository.getEmployeeByID(employeeId);

        assertThat(actualEmployee).isEqualTo(expectedEmployee);
    }

    @Test
    void tryingToCreateEmployeeWithTheSameEmailAsAnotherThrowsDuplicateKeyException() {
        saveNewEmployee();

        assertThatThrownBy(this::saveNewEmployee)
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void canUpdatePasswordForEmployee() {
        saveNewEmployee();
        Long employeeId = employeeRepository.getEmployeeIDByEmail(email);
        String newPassword = "newPassword";

        employeeRepository.updatePasswordForEmployee(employeeId, newPassword);

        Employee updatedEmployee = employeeRepository.getEmployeeByID(employeeId);
        assertThat(updatedEmployee.getPassword()).isEqualTo(newPassword);
    }

    private void saveNewEmployee() {
        Employee employee = new Employee(null, "Jane", "Doe", email, "password", EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());
        employeeRepository.save(employee);
    }

}
