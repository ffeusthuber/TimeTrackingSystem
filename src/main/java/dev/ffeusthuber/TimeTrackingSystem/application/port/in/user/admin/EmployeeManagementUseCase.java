package dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.EmployeeRole;

public interface EmployeeManagementUseCase {
    Employee createEmployee(Long employeeId, String firstname, String lastname, String email, String password, EmployeeRole role);
}
