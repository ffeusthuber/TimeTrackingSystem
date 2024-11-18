package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.adapter.in.InitialAdminCreator;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeManagementService;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeService;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WorkScheduleDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin.EmployeeManagementUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Tag("io")
public class JdbcEmployeeRepositoryTest {

    @MockBean
    InitialAdminCreator initialAdminCreator; //needed for application startUp

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    private EmployeeManagementUseCase employeeManagementService;
    private final String email = "j.doe@test-mail.com";

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
    void employeeIsSavedWhenCreatedViaEmployeeManagementService() {
        createEmployee();

        assertThat(employeeRepository.getAllEmployees()).hasSize(1);
    }

    @Test
    void canGetEmployeeIdFromEmail(){
        createEmployee();

        Long employeeId = employeeRepository.getEmployeeIDByEmail(email);

        assertThat(employeeId).isNotNull();
    }

    @Test
    void canGetEmployeeFromEmail(){
        createEmployee();

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
        Employee expectedEmployee = createEmployee();
        Long employeeId = employeeRepository.getEmployeeIDByEmail(email);

        Employee actualEmployee = employeeRepository.getEmployeeByID(employeeId);

        assertThat(actualEmployee).isEqualTo(expectedEmployee);
    }

    @Test
    void tryingToCreateEmployeeWithTheSameEmailAsAnotherThrowsDuplicateKeyException() {
        createEmployee();

        assertThatThrownBy(this::createEmployee)
                .isInstanceOf(DuplicateKeyException.class);
    }

    private Employee createEmployee() {
        return employeeManagementService.createEmployee("Jane", "Doe", email, "password", "USER", new WorkScheduleDTO(8, 8, 8, 8, 8, 0, 0));
    }
}
