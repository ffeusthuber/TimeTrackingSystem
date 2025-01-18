package dev.ffeusthuber.TimeTrackingSystem.application.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public Employee createEmployee(String firstname, String lastname, String email, String password, EmployeeRole employeeRole, WorkSchedule workSchedule) {
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        Employee employee = new Employee(null, firstname, lastname, email, encryptedPassword, employeeRole, workSchedule);

        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

    public Employee getEmployeeByID(long employeeID) {
        return employeeRepository.getEmployeeByID(employeeID)
                                 .orElseThrow(() -> new IllegalStateException("Employee not found"));
    }

    public ClockState getClockStateForEmployee(Long employeeID) {
        return employeeRepository.getEmployeeByID(employeeID)
                                 .map(Employee::getClockState)
                                 .orElseThrow(() -> new IllegalStateException("Employee not found"));
    }

    public boolean isEmployeeClockedIn(long employeeID) {
        return getClockStateForEmployee(employeeID) == ClockState.CLOCKED_IN;
    }

    public void setClockStateForEmployee(long employeeID, ClockState clockState) {
        employeeRepository.setClockStateForEmployee(employeeID, clockState.toString());
    }

    public float getScheduledHoursForEmployeeOnWeekDay(long employeeID, DayOfWeek dayOfWeek) {
        return employeeRepository.getEmployeeByID(employeeID)
                                 .map(employee -> employee.getWorkSchedule().getScheduledWorkHoursForDay(dayOfWeek))
                                 .orElseThrow(() -> new IllegalStateException("Employee not found"));
    }

    public void setPasswordForEmployee(long employeeId, String newPassword) {
        String encryptedPassword = bCryptPasswordEncoder.encode(newPassword);
        employeeRepository.updatePasswordForEmployee(employeeId, encryptedPassword);
    }

    public boolean isCorrectPasswordForEmployee(long employeeId, String password) {
        Employee employee = getEmployeeByID(employeeId);
        return bCryptPasswordEncoder.matches(password, employee.getPassword());
    }

    public void deleteEmployee(long employeeId) {
        employeeRepository.delete(employeeId);
    }
}
