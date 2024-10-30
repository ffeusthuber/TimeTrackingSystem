package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.adapter.out.WorkdayRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkdayServiceTest {


    @Test
    void canCreateWorkdayForEmployeeOnDate() {
        long employeeID = 1L;
        float workHoursFriday = 5.5f;
        WorkSchedule workSchedule = WorkSchedule.createSpecificWorkSchedule(8.5f, 8f, 8f, 8f, workHoursFriday, 0f, 0f);
        Employee employee = new Employee(employeeID, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, workSchedule);
        WorkdayRepository workdayRepository = WorkdayRepositoryStub.withoutWorkdays();
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withEmployee(employee);
        WorkdayService workdayService = new WorkdayService(employeeRepository, workdayRepository);
        ZonedDateTime workDate = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC); // date is a friday

        Workday workday = workdayService.getOrCreateWorkdayForEmployeeOnDate(employeeID, workDate);

        assertThat(workday.getEmployeeId()).isEqualTo(employeeID);
        assertThat(workday.getWorkDate()).isEqualTo(workDate);
        assertThat(workday.getScheduledHours()).isEqualTo(workHoursFriday);
    }

    @Test
    void ifWorkdayAlreadyExistsReturnExistingWorkday() {
        long employeeID = 1L;
        float scheduledHours = 5.5f;
        Workday workday = new Workday(employeeID, ZonedDateTime.now(), scheduledHours);
        WorkdayRepository workdayRepository = WorkdayRepositoryStub.withWorkday(workday);

        WorkdayService workdayService = new WorkdayService(null,workdayRepository);

        assertThat(workdayService.getOrCreateWorkdayForEmployeeOnDate(employeeID, ZonedDateTime.now())).isEqualTo(workday);
    }
}
