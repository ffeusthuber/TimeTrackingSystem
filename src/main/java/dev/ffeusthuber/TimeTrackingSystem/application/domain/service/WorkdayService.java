package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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

    public Optional<Workday> getWorkdayForEmployeeOnDate(long employeeID, LocalDate workDate) {
        return workdayRepository.getWorkdayForEmployeeOnDate(employeeID, workDate);
    }

    public Workday getOrCreateWorkdayForEmployeeOnDate(long employeeID, LocalDate workDate) {
        return getWorkdayForEmployeeOnDate(employeeID, workDate)
                .orElseGet(() -> createNewWorkdayForEmployee(employeeID, workDate));
    }

    private Workday createNewWorkdayForEmployee(long employeeID, LocalDate workDate) {
        float scheduledHours = getScheduledHoursForEmployeeOnDay(employeeID, workDate);

        return workdayRepository.saveWorkday(new Workday(employeeID, workDate, scheduledHours));
    }

    private float getScheduledHoursForEmployeeOnDay(long employeeID, LocalDate workDate) {
        Employee employee = employeeRepository.getEmployeeByID(employeeID);
        return employee.getWorkSchedule().getScheduledWorkHoursForDay(workDate.getDayOfWeek());
    }

    public void addTimeEntryToWorkday(TimeEntry timeEntry, Workday workday) {
        workday.addTimeEntry(timeEntry);
        workdayRepository.saveWorkday(workday);
    }

    public List<Workday> getWorkdaysForEmployeeBetweenDates(long employeeId, LocalDate fromIncluding, LocalDate toIncluding) {
        return workdayRepository.getWorkdaysForEmployeeBetweenDates(employeeId, fromIncluding, toIncluding);
    }
}
