package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.WorkdayRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WorkdayDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportServiceTest {

    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Europe/Vienna");
    private static final ZonedDateTime BASE_TIME = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    private static final long EMPLOYEE_ID_1 = 1L;

    @Test
    void canDisplayAllTimeEntriesOfLatestWorkdayOfEmployee() {
        TimeEntry timeEntry1 = new TimeEntry(EMPLOYEE_ID_1, TimeEntryType.CLOCK_IN, BASE_TIME);
        TimeEntry timeEntry2 = new TimeEntry(EMPLOYEE_ID_1, TimeEntryType.CLOCK_OUT, BASE_TIME.plusHours(8));
        List<TimeEntry> timeEntries = List.of(timeEntry1,timeEntry2);
        Workday workday = new Workday(1L, BASE_TIME.toLocalDate(), 8.5f);
        timeEntries.forEach(workday::addTimeEntry);
        ReportService reportService = new ReportService( new WorkdayService(null, WorkdayRepositoryStub.withWorkdays(workday)));

        List<TimeEntryDTO> timeEntriesForEmployee1 = reportService.getTimeEntriesOfLatestWorkdayOfEmployee(EMPLOYEE_ID_1, DEFAULT_ZONE);

        assertThat(timeEntriesForEmployee1).isEqualTo(List.of(new TimeEntryDTO(timeEntry1, DEFAULT_ZONE),
                                                              new TimeEntryDTO(timeEntry2, DEFAULT_ZONE)));
    }


    @Test
    void canDisplayDataOfLatestWorkdayOfEmployee() {
        float scheduledHours = 8.5f;
        float hoursWorked = 8;
        TimeEntry clockIn = new TimeEntry(EMPLOYEE_ID_1, TimeEntryType.CLOCK_IN, BASE_TIME);
        TimeEntry clockOut = new TimeEntry(EMPLOYEE_ID_1, TimeEntryType.CLOCK_OUT, BASE_TIME.plusHours((long) hoursWorked));
        Workday workday = new Workday(EMPLOYEE_ID_1, BASE_TIME.toLocalDate(), scheduledHours);
        workday.addTimeEntry(clockIn);
        workday.addTimeEntry(clockOut);
        WorkdayRepository workdayRepository = WorkdayRepositoryStub.withWorkdays(workday);
        WorkdayService workdayService = new WorkdayService(null, workdayRepository);
        ReportService reportService = new ReportService(workdayService);

        WorkdayDTO workdayDTO = reportService.getLatestWorkdayOfEmployee(EMPLOYEE_ID_1);

        assertThat(workdayDTO.getWorkedHours()).isEqualTo(hoursWorked);
    }

}
