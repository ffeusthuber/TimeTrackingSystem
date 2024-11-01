package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;


@Service
public class WorkdayService {

    private final EmployeeRepository employeeRepository;
    private final WorkdayRepository workdayRepository;

    public WorkdayService(EmployeeRepository employeeRepository, WorkdayRepository workdayRepository) {
        this.employeeRepository = employeeRepository;
        this.workdayRepository = workdayRepository;
    }

    public Optional<Workday> getLatestWorkdayForEmployee(long employeeID) {
        return workdayRepository.getLatestWorkdayForEmployee(employeeID);
    }

    public Optional<Workday> getWorkdayForEmployeeOnDate(long employeeID, LocalDate workDate, ZoneId zoneId) {
        return workdayRepository.getWorkdayForEmployeeOnDate(employeeID, workDate, zoneId);
    }

    private Workday getOrCreateWorkdayForEmployeeOnDate(long employeeID, LocalDate workDate, ZoneId zoneId) {
        return getWorkdayForEmployeeOnDate(employeeID, workDate, zoneId)
                .orElseGet(() -> {
                    Employee employee = employeeRepository.getEmployeeByID(employeeID);
                    float scheduledHours = employee.getWorkSchedule().getScheduledWorkHoursForDay(workDate.getDayOfWeek());
                    return new Workday(employeeID, workDate, zoneId, scheduledHours);
                });
    }

    public void addTimeEntryToWorkday(TimeEntry timeEntry) {
        Workday workday;
        if(timeEntry.getType() == TimeEntryType.CLOCK_IN) {
            workday = getOrCreateWorkdayForEmployeeOnDate(timeEntry.getEmployeeID(),
                                                          timeEntry.getEntryDateTime().toLocalDate(),
                                                          timeEntry.getEntryDateTime().getZone());
        }
        else {
            workday = getLatestWorkdayForEmployee(timeEntry.getEmployeeID())
                    .orElseThrow(() -> new IllegalStateException("No workday found for employee"));
        }
        workday.addTimeEntry(timeEntry);
        workdayRepository.saveWorkday(workday);
    }
}
