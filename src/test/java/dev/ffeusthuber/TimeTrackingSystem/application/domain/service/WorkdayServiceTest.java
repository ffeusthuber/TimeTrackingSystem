package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.adapter.out.WorkdayRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkdayServiceTest {

    @Test
    void firstAddedClockInTimeEntryCreatesWorkdayForEmployee() {
        long employeeID = 1L;
        ZonedDateTime timeOfFirstClockIn = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC); // date is a friday
        TimeEntry timeEntry = new TimeEntry(employeeID, TimeEntryType.CLOCK_IN, timeOfFirstClockIn);
        Employee employee = new Employee(employeeID, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());
        WorkdayRepository workdayRepository = WorkdayRepositoryStub.withoutWorkdays();
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withEmployee(employee);
        WorkdayService workdayService = new WorkdayService(employeeRepository, workdayRepository);

        assertThat(workdayRepository.getWorkdayForEmployeeOnDate(employeeID, timeOfFirstClockIn)).isEmpty();

        workdayService.addTimeEntryToWorkday(timeEntry);

        assertThat(workdayRepository.getWorkdayForEmployeeOnDate(employeeID, timeOfFirstClockIn)).isNotEmpty();
    }


    @Test
    void canGetExistingWorkdayForEmployeeOnDate() {
        long employeeID = 1L;
        float scheduledHours = 5.5f;
        Workday workday = new Workday(employeeID, ZonedDateTime.now(), scheduledHours);
        WorkdayRepository workdayRepository = WorkdayRepositoryStub.withWorkday(workday);

        WorkdayService workdayService = new WorkdayService(null,workdayRepository);

        assertThat(workdayService.getWorkdayForEmployeeOnDate(employeeID, ZonedDateTime.now())).isEqualTo(Optional.of(workday));
    }
}
