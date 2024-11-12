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
    public WeekReport getCurrentWeekReportForEmployee(long employeeId) {
        List<WorkdayDTO> workdays = getWorkdayDTOsForCurrentWeekForEmployee(employeeId);
        Employee employee = employeeService.getEmployeeById(employeeId);
        float scheduledHoursForWeek = employee.getWorkSchedule().getScheduledWorkHoursForWeek();
        float workedHours = getWorkedHoursForCurrentWeek(employeeId);

        return new WeekReport(getCurrentWeekNumber(), workdays, scheduledHoursForWeek, workedHours);
    }

    private List<WorkdayDTO> getWorkdayDTOsForCurrentWeekForEmployee(long employeeId) {
        return workdayService.getWorkdaysForEmployeeBetweenDates(employeeId, getStartOfWeek(), getEndOfWeek()).stream()
                             .map(WorkdayDTO::new)
                             .toList();
    }

    private float getWorkedHoursForCurrentWeek(long employeeId) {
        return workdayService.getWorkedHoursForEmployeeBetweenDates(employeeId, getStartOfWeek(), getEndOfWeek());
    }

    private int getCurrentWeekNumber() {
        return LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear());
    }

    private LocalDate getStartOfWeek() {
        return LocalDate.now().with(DayOfWeek.MONDAY);
    }

    private LocalDate getEndOfWeek() {
        return LocalDate.now().with(DayOfWeek.SUNDAY);
    }
}
