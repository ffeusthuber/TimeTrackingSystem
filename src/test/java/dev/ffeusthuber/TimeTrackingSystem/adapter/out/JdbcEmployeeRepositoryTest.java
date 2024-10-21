package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeManagementService;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeService;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin.EmployeeManagementUseCase;
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

import java.util.List;

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

    private EmployeeManagementUseCase employeeManagementService;

    @BeforeEach
    void setUp() {
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        employeeManagementService = new EmployeeManagementService(employeeService);
    }

    @AfterEach
    void clearDatabase() {
        jdbcTemplate.execute("DELETE FROM Work_schedule");
        jdbcTemplate.execute("DELETE FROM Employee");
    }

    @Test
    void employeeCanBeSaved() {
        employeeManagementService.createEmployee("Jane", "Doe", "j.doe@test-mail.com", "password", "USER");

        assertThat(employeeRepository.getEmployees()).hasSize(1);
    }

    @Test
    void canGetEmployeeIdFromEmail(){
        String email = "j.doe@test-mail.com";
        employeeManagementService.createEmployee("Jane", "Doe", email, "password", "USER");

        Long employeeId = employeeRepository.getEmployeeIDByEmail(email);

        assertThat(employeeId).isNotNull();
    }

    @Test
    void canGetEmployeeFromEmail(){
        String email = "j.doe@test-mail.com";
        employeeManagementService.createEmployee("Jane", "Doe", email, "password", "USER");

        Employee employee = employeeRepository.getEmployeeByEmail(email);

        assertThat(employee).isNotNull();
    }

    @Test
    void emptySearchResultsGetHandledCorrectly(){
        String notExistingEmail = "not existing email";
        long notExistingEmployeeId = -1L;

        Employee employee1 = employeeRepository.getEmployeeByEmail(notExistingEmail);
        Employee employee2 = employeeRepository.getEmployeeByID(notExistingEmployeeId);
        List<Employee> employees = employeeRepository.getEmployees();
        Long employeeId = employeeRepository.getEmployeeIDByEmail(notExistingEmail);

        assertThat(employee1).isNull();
        assertThat(employee2).isNull();
        assertThat(employees).isEmpty();
        assertThat(employeeId).isNull();
    }

    @Test
    void canGetEmployeeByID() {
        String email = "j.doe@test-mail.com";
        Employee expectedEmployee = employeeManagementService.createEmployee("Jane", "Doe", email, "password", "USER");
        Long employeeId = employeeRepository.getEmployeeIDByEmail(email);

        Employee actualEmployee = employeeRepository.getEmployeeByID(employeeId);
        System.out.println("WorkSchedule :" + actualEmployee.getWorkSchedule()); // why null?

        assertThat(actualEmployee).isEqualTo(expectedEmployee);
    }

    @Test
    void tryingToCreateEmployeeWithTheSameEmailAsAnotherThrowsDuplicateKeyException() {
        String email = "j.doe@test-mail.com";

        employeeManagementService.createEmployee("Jane", "Doe", email, "password", "USER");

        assertThatThrownBy(() -> employeeManagementService.createEmployee("John", "Doe", email, "password", "USER"))
                .isInstanceOf(DuplicateKeyException.class);
    }
}
