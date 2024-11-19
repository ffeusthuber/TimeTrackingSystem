package dev.ffeusthuber.TimeTrackingSystem.application.port.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;

import java.util.List;

public interface EmployeeRepository {

    void save(Employee employee);
    List<Employee> getAllEmployees();
    Employee getEmployeeByID(long employeeID);
    Employee getEmployeeByEmail(String email);
    Long getEmployeeIDByEmail(String email);
    void updatePasswordForEmployee(long employeeId, String encryptedPassword);
    void setClockStateForEmployee(Long employeeID, String clockState);
}
