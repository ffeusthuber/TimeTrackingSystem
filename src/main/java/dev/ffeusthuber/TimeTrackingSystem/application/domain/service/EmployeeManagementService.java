package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin.EmployeeManagementUseCase;

public class EmployeeManagementService implements EmployeeManagementUseCase {
    private final EmployeeService employeeService;

    public EmployeeManagementService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public Employee createEmployee(Long employeeID, String firstname, String lastname, String email, String password, EmployeeRole role) {
        Employee employee = employeeService.createEmployee(employeeID, firstname, lastname, email, password, role);
        return employee;
    }
}
