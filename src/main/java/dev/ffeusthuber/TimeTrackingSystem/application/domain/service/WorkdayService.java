package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class WorkdayService {

    private final EmployeeService employeeService;
    private final WorkdayRepository workdayRepository;

    public WorkdayService(EmployeeService employeeService, WorkdayRepository workdayRepository) {
        this.employeeService = employeeService;
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
                .orElseGet(() -> createAndSaveWorkday(employeeID, workDate));
    }

    private Workday createAndSaveWorkday(long employeeID, LocalDate workDate) {
        float scheduledHours = employeeService.getScheduledHoursForEmployeeOnWeekDay(employeeID, workDate.getDayOfWeek());
        Workday workday = new Workday(employeeID, workDate, scheduledHours);
        return workdayRepository.saveWorkday(workday);
    }

    public void addTimeEntryToWorkday(TimeEntry timeEntry, Workday workday) {
        workday.addTimeEntry(timeEntry);
        workdayRepository.saveWorkday(workday);
    }

    public List<Workday> getWorkdaysForEmployeeBetweenDates(long employeeId, LocalDate fromIncluding, LocalDate toIncluding) {
        return workdayRepository.getWorkdaysForEmployeeBetweenDates(employeeId, fromIncluding, toIncluding);
    }

    public float getWorkedHoursForEmployeeBetweenDates(long employeeId, LocalDate fromIncluding, LocalDate toIncluding) {
        return (float) getWorkdaysForEmployeeBetweenDates(employeeId, fromIncluding, toIncluding)
                .stream()
                .mapToDouble(Workday::calculateWorkedHours)
                .sum();
    }
}
