package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public ClockState getClockStateForEmployee(Long employeeID) {
        Employee employee = employeeRepository.getEmployeeByID(employeeID);
        return employee.getClockState();
    }

    public boolean isEmployeeClockedIn(long employeeID) {
        return getClockStateForEmployee(employeeID) == ClockState.CLOCKED_IN;
    }

    public void setClockStateForEmployee(long employeeID, ClockState clockState) {
        Employee employee = employeeRepository.getEmployeeByID(employeeID);

        switch(clockState) {
            case CLOCKED_IN:
                employee.clockIn();
                break;
            case CLOCKED_OUT:
                employee.clockOut();
                break;
            case ON_PAUSE:
                employee.clockPause();
                break;
        }

        employeeRepository.updateClockState(employeeID, clockState.toString());
    }

    public Employee createEmployee(String firstname, String lastname, String email, String password, String role) {
        EmployeeRole employeeRole = EmployeeRole.valueOf(role);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        Employee employee = new Employee(null, firstname, lastname, email, encryptedPassword, employeeRole, WorkSchedule.createDefaultWorkSchedule());

        employeeRepository.create(employee);
        Long employeeId = employeeRepository.getEmployeeIDByEmail(email);

        return new Employee(employeeId, firstname, lastname, email, encryptedPassword, employeeRole, WorkSchedule.createDefaultWorkSchedule());
    }

    public Employee getEmployeeById(long employeeID) {
        return employeeRepository.getEmployeeByID(employeeID);
    }

    public float getScheduledHoursForEmployeeOnWeekDay(long employeeId, DayOfWeek dayOfWeek) {
        Employee employee = employeeRepository.getEmployeeByID(employeeId);
        return employee.getWorkSchedule().getScheduledWorkHoursForDay(dayOfWeek);
    }
}
