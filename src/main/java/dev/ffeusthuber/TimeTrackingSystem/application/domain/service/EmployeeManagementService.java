package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.EmployeeDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin.EmployeeManagementUseCase;
import org.springframework.stereotype.Service;

@Service
public class EmployeeManagementService implements EmployeeManagementUseCase {
    private final EmployeeService employeeService;

    public EmployeeManagementService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public Employee createEmployee(String firstname, String lastname, String email, String password, String role) {
        return employeeService.createEmployee(firstname, lastname, email, password, role);
    }

    @Override
    public EmployeeDTO getEmployee(long employeeID) {
        return new EmployeeDTO(employeeService.getEmployeeById(employeeID));
    }
}
