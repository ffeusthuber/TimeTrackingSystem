package dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.EmployeeDTO;

public interface EmployeeManagementUseCase {
    Employee createEmployee(String firstname, String lastname, String email, String password, String role);
    EmployeeDTO getEmployee(long employeeID);
}
