package dev.ffeusthuber.TimeTrackingSystem.application.port.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;

public interface EmployeeRepository {
    Employee getEmployeeById(long employeeID);
}
