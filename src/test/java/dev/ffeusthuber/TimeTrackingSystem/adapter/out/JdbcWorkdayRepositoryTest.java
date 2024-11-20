package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.adapter.in.InitialAdminCreator;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Tag("io")
public class JdbcWorkdayRepositoryTest {

    @MockBean
    InitialAdminCreator initialAdminCreator; //needed for application startUp

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
    void saveWorkdayInsertsNewWorkdayAndSetsWorkdayId() {
        Workday workday = new Workday(EMPLOYEE_ID, LocalDate.of(2020, 1, 1), 5.5f);
        Workday savedWorkday = workdayRepository.save(workday);

        assertThat(savedWorkday).isNotNull();
        assertThat(savedWorkday.getWorkdayId()).isNotNull();
    }

    @Test
    void saveWorkdayDoesNotInsertDuplicate() {
        Workday workday = new Workday(EMPLOYEE_ID, LocalDate.of(2020, 1, 1), 5.5f);
        workdayRepository.save(workday);
        Workday duplicateWorkday = new Workday(EMPLOYEE_ID, LocalDate.of(2020, 1, 1), 6.0f);
        Workday savedWorkday = workdayRepository.save(duplicateWorkday);

        assertThat(savedWorkday.getScheduledHours()).isEqualTo(5.5f);
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

    @Test
    void getWorkdayForEmployeeOnDateReturnsEmptyIfNotFound() {
        assertThat(workdayRepository.getWorkdayForEmployeeOnDate(EMPLOYEE_ID, LocalDate.of(2020, 1, 1)))
                .isNotPresent();
    }

    @Test
    void getLatestWorkdayForEmployeeReturnsEmptyIfNotFound() {
        assertThat(workdayRepository.getLatestWorkdayForEmployee(EMPLOYEE_ID))
                .isNotPresent();
    }

    @Test
    void canGetWorkdaysBetweenTwoDates() {
        Workday workday1 = new Workday(EMPLOYEE_ID, LocalDate.of(2021, 1, 1), 5.5f);
        Workday workday2 = new Workday(EMPLOYEE_ID, LocalDate.of(2021, 1, 2), 5.5f);
        Workday workday3 = new Workday(EMPLOYEE_ID, LocalDate.of(2021, 1, 3), 5.5f);

        saveWorkdaysToRepository(workday1, workday2, workday3);

        assertThat(workdayRepository.getWorkdaysForEmployeeBetweenDates(EMPLOYEE_ID, LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 3)))
                .isEqualTo(List.of(workday1, workday2, workday3));
    }

    @Test
    void noFoundWorkdayBetweenDatesReturnsEmptyList() {
        assertThat(workdayRepository.getWorkdaysForEmployeeBetweenDates(EMPLOYEE_ID, LocalDate.of(2021, 1, 4), LocalDate.of(2021, 1, 5)))
                .isEmpty();
    }

    private void saveWorkdaysToRepository(Workday... workdays) {
        for (Workday workday : workdays) {
            workdayRepository.save(workday);
        }
    }
}
