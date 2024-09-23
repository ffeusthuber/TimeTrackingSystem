package dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;

public interface EmployeeManagementUseCase {
    Employee createEmployee(String firstname, String lastname, String email, String password, String role);
}
