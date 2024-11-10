package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeService;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.TimeEntryService;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.TimeTrackingService;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.WorkdayService;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.TimeTrackingUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.TimeEntryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Tag("io")
public class JdbcTimeEntryRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TimeEntryRepository timeEntryRepository;


    private TimeTrackingUseCase timeTrackingService;

    private long employeeID;

    @BeforeEach
    void setUp() {
        employeeID = 1;
        Employee employee = new Employee(employeeID, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());
        TimeEntryService timeEntryService = new TimeEntryService(timeEntryRepository);
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withEmployee(employee);
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        WorkdayService workdayService = new WorkdayService(employeeRepository, WorkdayRepositoryStub.withoutWorkdays());
        timeTrackingService = new TimeTrackingService(timeEntryService, employeeService, workdayService);
    }

    @AfterEach
    void clearDatabase() {
        jdbcTemplate.execute("DELETE FROM Employee");
    }

    @Disabled
    @Test
    void timeEntryIsSavedWhenCreatedWithTimeTrackingService() {
        timeTrackingService.clockIn(employeeID);

        assertThat(timeEntryRepository.getTimeEntriesByEmployeeId(employeeID)).hasSize(1);
    }

}
