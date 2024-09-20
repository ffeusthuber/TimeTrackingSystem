package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockState;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public ClockState getClockStateForEmployee(Long employeeID) {
        Employee employee = employeeRepository.getEmployeeById(employeeID);
        return employee.getClockState();
    }

    public void setClockStateForEmployee(long employeeID, ClockState clockState) {
        Employee employee = employeeRepository.getEmployeeById(employeeID);

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
    }
}
