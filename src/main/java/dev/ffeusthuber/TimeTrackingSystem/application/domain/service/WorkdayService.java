package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;


@Service
public class WorkdayService {

    private final EmployeeRepository employeeRepository;
    private final WorkdayRepository workdayRepository;

    public WorkdayService(EmployeeRepository employeeRepository, WorkdayRepository workdayRepository) {
        this.employeeRepository = employeeRepository;
        this.workdayRepository = workdayRepository;
    }

    public Workday getOrCreateWorkdayForEmployeeOnDate(long employeeID, ZonedDateTime workDate) {
        Optional<Workday> existingWorkday = workdayRepository.getWorkdayForEmployeeOnDate(employeeID, workDate);
        if (existingWorkday.isPresent()) {
            return existingWorkday.get();
        }
        Employee employee = employeeRepository.getEmployeeByID(employeeID);
        float scheduledHours = employee.getWorkSchedule().getScheduledWorkHoursForDay(workDate.getDayOfWeek());
        return new Workday(employeeID, workDate, scheduledHours);
    }
}
