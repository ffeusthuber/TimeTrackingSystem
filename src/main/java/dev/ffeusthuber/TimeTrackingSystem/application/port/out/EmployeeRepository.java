package dev.ffeusthuber.TimeTrackingSystem.application.port.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;

import java.util.List;

public interface EmployeeRepository {

    void save(Employee employee);

    List<Employee> getEmployees();

    Employee getEmployeeById(long employeeID);

    Long getEmployeeIdByEmail(String email);
}
