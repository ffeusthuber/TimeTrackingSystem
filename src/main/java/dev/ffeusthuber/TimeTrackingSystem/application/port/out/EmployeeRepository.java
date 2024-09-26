package dev.ffeusthuber.TimeTrackingSystem.application.port.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;

import java.util.List;

public interface EmployeeRepository {

    void save(Employee employee);

    List<Employee> getEmployees();

    Employee getEmployeeByID(long employeeID);

    Long getEmployeeIDByEmail(String email);

    Employee getEmployeeByEmail(String email);
}
