package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.adapter.out.WorkdayRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkdayServiceTest {

    private final long employeeId = 1L;
    private Employee employee;
    private WorkdayService workdayService;

    @BeforeEach
    void setup() {
        employee = createEmployee(employeeId);
        workdayService = new WorkdayService(
                EmployeeRepositoryStub.withEmployee(employee),
                WorkdayRepositoryStub.withoutWorkdays()
        );
    }

    @Test
    void canGetLatestWorkdayOfEmployee() {
        workdayService.getOrCreateWorkdayForEmployeeOnDate(employeeId, LocalDate.of(2021, 1, 1));
        workdayService.getOrCreateWorkdayForEmployeeOnDate(employeeId, LocalDate.of(2021, 1, 2));

        Optional<Workday> latestWorkday = workdayService.getLatestWorkdayForEmployee(employeeId);

        assertThat(latestWorkday).isPresent();
        assertThat(latestWorkday.get().getWorkDate()).isEqualTo(LocalDate.of(2021, 1, 2));
    }


    @Test
    void canGetWorkdayForEmployeeOnSpecificDate() {
        Workday workday = workdayService.getOrCreateWorkdayForEmployeeOnDate(employeeId, LocalDate.of(2021, 1, 1));

        Optional<Workday> retrievedWorkday = workdayService.getWorkdayForEmployeeOnDate(employeeId, LocalDate.of(2021, 1, 1));

        assertThat(retrievedWorkday).isPresent();
        assertThat(retrievedWorkday.get()).isEqualTo(workday);
    }

    @Test
    void whenTryingToGetWorkdayOnDateWithoutExistingWorkdayNoWorkdayIsReturned() {
        Optional<Workday> workday = workdayService.getWorkdayForEmployeeOnDate(employeeId, LocalDate.of(2021, 1, 1));

        assertThat(workday).isNotPresent();
    }

    @Test
    void canGetWorkdaysBetweenTwoDates() {
        Workday workday0 = workdayService.getOrCreateWorkdayForEmployeeOnDate(employeeId, LocalDate.of(2021, 1, 1));
        Workday workday1 = workdayService.getOrCreateWorkdayForEmployeeOnDate(employeeId, LocalDate.of(2021, 1, 2));
        Workday workday2 = workdayService.getOrCreateWorkdayForEmployeeOnDate(employeeId, LocalDate.of(2021, 1, 3));
        Workday workday3 = workdayService.getOrCreateWorkdayForEmployeeOnDate(employeeId, LocalDate.of(2021, 1, 4));
        Workday workday4 = workdayService.getOrCreateWorkdayForEmployeeOnDate(employeeId, LocalDate.of(2021, 1, 5));

        List<Workday> workdays = workdayService.getWorkdaysForEmployeeBetweenDates(employeeId, LocalDate.of(2021, 1, 2), LocalDate.of(2021, 1, 4));

        assertThat(workdays).containsExactly(workday1, workday2, workday3);
    }

    @Test
    void canAddTimeEntryToWorkday() {
        Workday workday = workdayService.getOrCreateWorkdayForEmployeeOnDate(employeeId, LocalDate.of(2021, 1, 1));
        TimeEntry timeEntry = new TimeEntry(employeeId, TimeEntryType.CLOCK_IN, ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")));

        workdayService.addTimeEntryToWorkday(timeEntry, workday);

        assertThat(workday.getTimeEntries()).contains(timeEntry);
    }


    @Test
    void canGetWorkedHoursForEmployeeBetweenDates() {
        float expectedWorkedHours = 16.0f;
        Workday workday1 = workdayService.getOrCreateWorkdayForEmployeeOnDate(employeeId, LocalDate.of(2021, 1, 1));
        Workday workday2 = workdayService.getOrCreateWorkdayForEmployeeOnDate(employeeId, LocalDate.of(2021, 1, 3));

        ZonedDateTime clockInDay1 = ZonedDateTime.of(2021, 1, 1, 9, 0, 0, 0, ZoneId.of("UTC"));
        ZonedDateTime clockInDay2 = ZonedDateTime.of(2021, 1, 3, 9, 0, 0, 0, ZoneId.of("UTC"));

        workday1.addTimeEntry(new TimeEntry(employeeId, TimeEntryType.CLOCK_IN, clockInDay1));
        workday1.addTimeEntry(new TimeEntry(employeeId, TimeEntryType.CLOCK_OUT, clockInDay1.plusHours((long)expectedWorkedHours / 2)));

        workday2.addTimeEntry(new TimeEntry(employeeId, TimeEntryType.CLOCK_IN, clockInDay2));
        workday2.addTimeEntry(new TimeEntry(employeeId, TimeEntryType.CLOCK_OUT, clockInDay2.plusHours((long)expectedWorkedHours / 2)));

        float workedHours = workdayService.getWorkedHoursForEmployeeBetweenDates(employeeId, LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 3));

        assertThat(workedHours).isEqualTo(expectedWorkedHours);
    }

    private Employee createEmployee(long employeeId) {
        return new Employee(employeeId, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());
    }
}
