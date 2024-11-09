package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Tag("io")
public class JdbcWorkdayRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private WorkdayRepository workdayRepository;

    private static final long EMPLOYEE_ID = 1L;

    @AfterEach
    void clearDatabase() {
        jdbcTemplate.execute("DELETE FROM Workday");
    }

    @Test
    void canGetLatestWorkdayForEmployee() {
        Workday workday = new Workday(EMPLOYEE_ID, LocalDate.of(2020, 1, 1), 5.5f);
        Workday latestWorkday = new Workday(EMPLOYEE_ID, LocalDate.of(2020,1,2),  5.5f);
        saveWorkdaysToRepository(workday, latestWorkday);

        assertThat(workdayRepository.getLatestWorkdayForEmployee(EMPLOYEE_ID))
                .isPresent()
                .contains(latestWorkday);
    }

    @Test
    void canGetExistingWorkdayForEmployeeOnDate() {
        LocalDate workDate = LocalDate.of(2020,1,1);
        Workday workday = new Workday(EMPLOYEE_ID, workDate, 5.5f);
        saveWorkdaysToRepository(workday);

        assertThat(workdayRepository.getWorkdayForEmployeeOnDate(EMPLOYEE_ID, workDate))
                .isPresent()
                .contains(workday);
    }

    private void saveWorkdaysToRepository(Workday... workdays) {
        for (Workday workday : workdays) {
            workdayRepository.saveWorkday(workday);
        }
    }
}
