package dev.ffeusthuber.TimeTrackingSystem.application.port.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {

    Employee save(Employee employee);
    void delete(long employeeId);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeByID(long employeeID);
    Optional<Employee> getEmployeeByEmail(String email);
    Long getEmployeeIDByEmail(String email);
    void updatePasswordForEmployee(long employeeId, String encryptedPassword);
    void setClockStateForEmployee(Long employeeID, String clockState);
}
