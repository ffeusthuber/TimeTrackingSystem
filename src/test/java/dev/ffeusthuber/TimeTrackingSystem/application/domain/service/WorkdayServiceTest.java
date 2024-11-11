package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.adapter.out.WorkdayRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkdayServiceTest {

    private static final long EMPLOYEE_ID = 1L;

    @Test
    void canGetLatestWorkdayOfEmployee() {
        Employee employee = createEmployee();
        WorkdayService workdayService = new WorkdayService(
                EmployeeRepositoryStub.withEmployee(employee),
                WorkdayRepositoryStub.withoutWorkdays());

        workdayService.getOrCreateWorkdayForEmployeeOnDate(EMPLOYEE_ID, LocalDate.of(2021, 1, 1));
        workdayService.getOrCreateWorkdayForEmployeeOnDate(EMPLOYEE_ID, LocalDate.of(2021, 1, 2));

        Optional<Workday> latestWorkday = workdayService.getLatestWorkdayForEmployee(EMPLOYEE_ID);

        assertThat(latestWorkday).isPresent();
        assertThat(latestWorkday.get().getWorkDate()).isEqualTo(LocalDate.of(2021, 1, 2));
    }


    @Test
    void canGetWorkdayForEmployeeOnSpecificDate() {
        Employee employee = createEmployee();
        WorkdayService workdayService = new WorkdayService(
                EmployeeRepositoryStub.withEmployee(employee),
                WorkdayRepositoryStub.withoutWorkdays());

        Workday workday = workdayService.getOrCreateWorkdayForEmployeeOnDate(EMPLOYEE_ID, LocalDate.of(2021, 1, 1));

        Optional<Workday> retrievedWorkday = workdayService.getWorkdayForEmployeeOnDate(EMPLOYEE_ID, LocalDate.of(2021, 1, 1));

        assertThat(retrievedWorkday).isPresent();
        assertThat(retrievedWorkday.get()).isEqualTo(workday);
    }

    @Test
    void whenTryingToGetWorkdayOnDateWithoutExistingWorkdayNoWorkdayIsReturned() {
        Employee employee = createEmployee();
        WorkdayService workdayService = new WorkdayService(
                EmployeeRepositoryStub.withEmployee(employee),
                WorkdayRepositoryStub.withoutWorkdays());

        Optional<Workday> workday = workdayService.getWorkdayForEmployeeOnDate(EMPLOYEE_ID, LocalDate.of(2021, 1, 1));

        assertThat(workday).isNotPresent();
    }

    @Test
    void canAddTimeEntryToWorkday() {
        Employee employee = createEmployee();
        WorkdayService workdayService = new WorkdayService(
                EmployeeRepositoryStub.withEmployee(employee),
                WorkdayRepositoryStub.withoutWorkdays());

        Workday workday = workdayService.getOrCreateWorkdayForEmployeeOnDate(EMPLOYEE_ID, LocalDate.of(2021, 1, 1));
        TimeEntry timeEntry = new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_IN, ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")));
        workdayService.addTimeEntryToWorkday(timeEntry, workday);

        assertThat(workday.getTimeEntries()).contains(timeEntry);
    }

    private Employee createEmployee() {
        return new Employee(EMPLOYEE_ID, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());
    }
}
