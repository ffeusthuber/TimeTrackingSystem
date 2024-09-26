package dev.ffeusthuber.TimeTrackingSystem.application.port.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;

import java.util.List;

public interface EmployeeRepository {

    void create(Employee employee);

    void updateClockState(Long employeeID, String clockState);

    List<Employee> getEmployees();

    Employee getEmployeeByID(long employeeID);

    Long getEmployeeIDByEmail(String email);

    Employee getEmployeeByEmail(String email);


}
