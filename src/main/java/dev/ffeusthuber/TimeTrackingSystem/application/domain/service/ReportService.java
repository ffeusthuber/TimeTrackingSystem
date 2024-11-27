package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WeekReport;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WorkdayDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService implements ReportUseCase {
    private final WorkdayService workdayService;
    private final EmployeeService employeeService;

    public ReportService(WorkdayService workdayService, EmployeeService employeeService) {
        this.workdayService = workdayService;
        this.employeeService = employeeService;
    }

    @Override
    public List<TimeEntryDTO> getTimeEntriesOfLatestWorkdayOfEmployee(long employeeID, ZoneId zoneId) {
        Optional<Workday> optionalWorkday = workdayService.getLatestWorkdayForEmployee(employeeID);
        return optionalWorkday.map(workday -> workday.getTimeEntries().stream()
                                                     .map(timeEntry -> new TimeEntryDTO(timeEntry, zoneId))
                                                     .toList())
                              .orElse(Collections.emptyList());
    }

    @Override
    public WeekReport getWeekReportForEmployeeAndWeekOfYear(long employeeId, int weekNumber, int year) {
        WeekOfYear weekOfYear = new WeekOfYear(weekNumber, year);
        List<WorkdayDTO> workdays = getWorkdayDTOsForWeekForEmployee(employeeId, weekOfYear);
        float scheduledHoursForWeek = getScheduledHoursForWeek(employeeId);
        float workedHours = getWorkedHoursForWeek(employeeId, weekOfYear);

        return new WeekReport(weekOfYear, workdays, scheduledHoursForWeek, workedHours);
    }

    private List<WorkdayDTO> getWorkdayDTOsForWeekForEmployee(long employeeId, WeekOfYear weekOfYear) {
        return workdayService.getWorkdaysForEmployeeBetweenDates(employeeId, getStartOfWeek(weekOfYear), getEndOfWeek(weekOfYear)).stream()
                             .map(WorkdayDTO::new)
                             .toList();
    }

    private float getScheduledHoursForWeek(long employeeId) {
        Employee employee = employeeService.getEmployeeByID(employeeId);
        return employee.getWorkSchedule().getScheduledWorkHoursForWeek();
    }

    private float getWorkedHoursForWeek(long employeeId, WeekOfYear weekOfYear) {
        return workdayService.getWorkedHoursForEmployeeBetweenDates(employeeId, getStartOfWeek(weekOfYear), getEndOfWeek(weekOfYear));
    }

    private LocalDate getStartOfWeek(WeekOfYear weekOfYear) {
        return LocalDate.now()
                        .withYear(weekOfYear.getYear())
                        .with(WeekFields.ISO.weekOfWeekBasedYear(), weekOfYear.getWeekNumber())
                        .with(DayOfWeek.MONDAY);
    }

    private LocalDate getEndOfWeek(WeekOfYear weekOfYear) {
        return LocalDate.now()
                        .withYear(weekOfYear.getYear())
                        .with(WeekFields.ISO.weekOfWeekBasedYear(), weekOfYear.getWeekNumber())
                        .with(DayOfWeek.SUNDAY);
    }
}
