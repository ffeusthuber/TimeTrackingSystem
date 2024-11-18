package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.setup.CreateInitialAdminUseCase;
import org.springframework.stereotype.Service;

@Service
public class CreateInitialAdminService implements CreateInitialAdminUseCase {
    private final EmployeeService employeeService;

    public CreateInitialAdminService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void createInitialAdmin(String firstName, String lastName, String email, String password) {
        if(employeeService.getAllEmployees().isEmpty()){
            employeeService.createEmployee(firstName, lastName, email, password, EmployeeRole.ADMIN, WorkSchedule.createDefaultWorkSchedule());
        }
    }
}
