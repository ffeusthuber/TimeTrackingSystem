package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeService;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.TimeEntryService;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.TimeTrackingService;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.TimeTrackingUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.TimeEntryRepository;
import dev.ffeusthuber.TimeTrackingSystem.config.WorkScheduleConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
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
        WorkScheduleConfig workScheduleConfig = new WorkScheduleConfig();
        Employee employee = new Employee(employeeID, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, workScheduleConfig.defaultWorkSchedule());
        TimeEntryService timeEntryService = new TimeEntryService(timeEntryRepository);
        EmployeeService employeeService = new EmployeeService(EmployeeRepositoryStub.withEmployee(employee));
        timeTrackingService = new TimeTrackingService(timeEntryService, employeeService);
    }

    @AfterEach
    void clearDatabase() {
        jdbcTemplate.execute("DELETE FROM Employee");
    }

    @Test
    void timeEntryCanBeSaved() {
        timeTrackingService.clockIn(employeeID);

        assertThat(timeEntryRepository.getTimeEntriesByEmployeeId(employeeID)).hasSize(1);
    }

}
