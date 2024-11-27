package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.adapter.out.WorkdayRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WeekReport;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportServiceTest {

    private final ZoneId defaultZone = ZoneId.of("Europe/Vienna");
    private final ZonedDateTime baseTime = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    private final long employeeId = 1L;
    private final float scheduledHours = 8.5f;

    @Test
    void canDisplayAllTimeEntriesOfLatestWorkdayOfEmployee() {
        TimeEntry timeEntry1 = new TimeEntry(employeeId, TimeEntryType.CLOCK_IN, baseTime);
        TimeEntry timeEntry2 = new TimeEntry(employeeId, TimeEntryType.CLOCK_OUT, baseTime.plusHours(8));
        List<TimeEntry> timeEntries = List.of(timeEntry1,timeEntry2);
        Workday workday = new Workday(employeeId, baseTime.toLocalDate(), scheduledHours);
        timeEntries.forEach(workday::addTimeEntry);
        ReportUseCase reportService = new ReportService(new WorkdayService(null, WorkdayRepositoryStub.withWorkdays(workday)), null);

        List<TimeEntryDTO> timeEntriesForEmployee = reportService.getTimeEntriesOfLatestWorkdayOfEmployee(employeeId, defaultZone);

        assertThat(timeEntriesForEmployee).isEqualTo(List.of(new TimeEntryDTO(timeEntry1, defaultZone),
                                                              new TimeEntryDTO(timeEntry2, defaultZone)));
    }

    @Test
    void ifNoWorkdayExistsTimeEntriesOfLatestWorkdayReturnsEmptyList() {
        WorkdayRepository workdayRepository = WorkdayRepositoryStub.withoutWorkdays();
        WorkdayService workdayService = new WorkdayService(null, workdayRepository);
        ReportUseCase reportService = new ReportService(workdayService, null);

        List<TimeEntryDTO> timeEntriesForEmployee1 = reportService.getTimeEntriesOfLatestWorkdayOfEmployee(employeeId, defaultZone);

        assertThat(timeEntriesForEmployee1).isEmpty();
    }

    @Test
    void canGetWeekReportForSpecificWeekForEmployee() {
        int weekNumber = LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear());
        int year = LocalDate.now().getYear();
        System.out.println(weekNumber);
        System.out.println(year);
        Workday workday = new Workday(employeeId, LocalDate.now(), scheduledHours);
        WorkSchedule workSchedule = WorkSchedule.createDefaultWorkSchedule();
        Employee employee = new Employee(employeeId, ClockState.CLOCKED_OUT, workSchedule);
        EmployeeService employeeService = new EmployeeService(EmployeeRepositoryStub.withEmployee(employee));
        WorkdayService workdayService = new WorkdayService(employeeService, WorkdayRepositoryStub.withWorkdays(workday));
        ReportUseCase reportService = new ReportService(workdayService, employeeService);

        WeekReport weekReport = reportService.getWeekReportForEmployeeAndWeekOfYear(employeeId, weekNumber, year);

        assertThat(weekReport).isNotNull();
        assertThat(weekReport.workdays()).isNotEmpty();
        assertThat(weekReport.scheduledWorkHoursForWeek()).isEqualTo(workSchedule.getScheduledWorkHoursForWeek());
        assertThat(weekReport.workedHoursForWeek()).isEqualTo(0.0f);
        assertThat(weekReport.weekOfYear().getWeekNumber()).isEqualTo(weekNumber);
        assertThat(weekReport.weekOfYear().getYear()).isEqualTo(year);
    }

    @Test
    void canGetWeekReportForSpecificWeekOfDifferentYearForEmployee() {
        int weekNumber = LocalDate.now().minusYears(1).get(WeekFields.ISO.weekOfWeekBasedYear());
        int year = LocalDate.now().minusYears(1).getYear();
        Workday workday = new Workday(employeeId, LocalDate.now(), scheduledHours);
        WorkSchedule workSchedule = WorkSchedule.createDefaultWorkSchedule();
        Employee employee = new Employee(employeeId, ClockState.CLOCKED_OUT, workSchedule);
        EmployeeService employeeService = new EmployeeService(EmployeeRepositoryStub.withEmployee(employee));
        WorkdayService workdayService = new WorkdayService(employeeService, WorkdayRepositoryStub.withWorkdays(workday));
        ReportUseCase reportService = new ReportService(workdayService, employeeService);

        WeekReport weekReport = reportService.getWeekReportForEmployeeAndWeekOfYear(employeeId, weekNumber, year);

        assertThat(weekReport).isNotNull();
        assertThat(weekReport.workdays()).isEmpty();
        assertThat(weekReport.scheduledWorkHoursForWeek()).isEqualTo(workSchedule.getScheduledWorkHoursForWeek());
        assertThat(weekReport.workedHoursForWeek()).isEqualTo(0.0f);
        assertThat(weekReport.weekOfYear().getWeekNumber()).isEqualTo(weekNumber);
        assertThat(weekReport.weekOfYear().getYear()).isEqualTo(year);
    }
}
