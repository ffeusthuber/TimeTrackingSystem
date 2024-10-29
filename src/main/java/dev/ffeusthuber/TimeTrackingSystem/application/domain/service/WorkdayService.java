package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;


@Service
public class WorkdayService {

    private final EmployeeRepository employeeRepository;

    public WorkdayService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    public Workday createWorkdayForEmployeeOnDate(long employeeID, ZonedDateTime workDate) {
        Employee employee = employeeRepository.getEmployeeByID(employeeID);
        float scheduledHours = employee.getWorkSchedule().getScheduledWorkHoursForDay(workDate.getDayOfWeek());
        return new Workday(employeeID, workDate, scheduledHours);
    }
}
