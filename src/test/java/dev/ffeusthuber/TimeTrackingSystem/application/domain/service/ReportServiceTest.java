package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.WorkdayRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WorkdayDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportServiceTest {

    private final ZoneId defaultZone = ZoneId.of("Europe/Vienna");
    private final ZonedDateTime baseTime = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    private final long employeeId1 = 1L;
    private final float scheduledHours = 8.5f;

    @Test
    void canDisplayAllTimeEntriesOfLatestWorkdayOfEmployee() {
        TimeEntry timeEntry1 = new TimeEntry(employeeId1, TimeEntryType.CLOCK_IN, baseTime);
        TimeEntry timeEntry2 = new TimeEntry(employeeId1, TimeEntryType.CLOCK_OUT, baseTime.plusHours(8));
        List<TimeEntry> timeEntries = List.of(timeEntry1,timeEntry2);
        Workday workday = new Workday(1L, baseTime.toLocalDate(), scheduledHours);
        timeEntries.forEach(workday::addTimeEntry);
        ReportUseCase reportService = new ReportService( new WorkdayService(null, WorkdayRepositoryStub.withWorkdays(workday)));

        List<TimeEntryDTO> timeEntriesForEmployee1 = reportService.getTimeEntriesOfLatestWorkdayOfEmployee(employeeId1, defaultZone);

        assertThat(timeEntriesForEmployee1).isEqualTo(List.of(new TimeEntryDTO(timeEntry1, defaultZone),
                                                              new TimeEntryDTO(timeEntry2, defaultZone)));
    }

    @Test
    void ifNoWorkdayExistsTimeEntriesOfLatestWorkdayReturnsEmptyList() {
        WorkdayRepository workdayRepository = WorkdayRepositoryStub.withoutWorkdays();
        WorkdayService workdayService = new WorkdayService(null, workdayRepository);
        ReportUseCase reportService = new ReportService(workdayService);

        List<TimeEntryDTO> timeEntriesForEmployee1 = reportService.getTimeEntriesOfLatestWorkdayOfEmployee(employeeId1, defaultZone);

        assertThat(timeEntriesForEmployee1).isEmpty();
    }

    @Test
    void canDisplayDataOfLatestWorkdayOfEmployee() {
        float hoursWorked = 8;
        TimeEntry clockIn = new TimeEntry(employeeId1, TimeEntryType.CLOCK_IN, baseTime);
        TimeEntry clockOut = new TimeEntry(employeeId1, TimeEntryType.CLOCK_OUT, baseTime.plusHours((long) hoursWorked));
        List<TimeEntry> timeEntries = List.of(clockIn,clockOut);
        Workday workday = new Workday(employeeId1, baseTime.toLocalDate(), scheduledHours);
        timeEntries.forEach(workday::addTimeEntry);
        WorkdayRepository workdayRepository = WorkdayRepositoryStub.withWorkdays(workday);
        WorkdayService workdayService = new WorkdayService(null, workdayRepository);
        ReportUseCase reportService = new ReportService(workdayService);

        WorkdayDTO workdayDTO = reportService.getWorkdayDataForLatestWorkdayOfEmployee(employeeId1);

        assertThat(workdayDTO).isNotNull();
    }

    @Test
    void ifNoWorkdayExistsReturnNullForLatestWorkday() {
        WorkdayRepository workdayRepository = WorkdayRepositoryStub.withoutWorkdays();
        WorkdayService workdayService = new WorkdayService(null, workdayRepository);
        ReportUseCase reportService = new ReportService(workdayService);

        WorkdayDTO workdayDTO = reportService.getWorkdayDataForLatestWorkdayOfEmployee(employeeId1);

        assertThat(workdayDTO).isNull();
    }

    @Test
    void canDisplayWorkdaysForCurrentWeek() {
        Workday workday = new Workday(employeeId1, LocalDate.now(), scheduledHours);
        WorkdayRepository workdayRepository = WorkdayRepositoryStub.withWorkdays(workday);
        WorkdayService workdayService = new WorkdayService(null, workdayRepository);
        ReportUseCase reportService = new ReportService(workdayService);

        List<WorkdayDTO> workdaysForCurrentWeek = reportService.getWorkdayDataForCurrentWeekForEmployee(employeeId1);

        assertThat(workdaysForCurrentWeek).isNotEmpty();
    }
}
