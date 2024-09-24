package dev.ffeusthuber.TimeTrackingSystem.setup;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeService;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetupService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeService employeeService;
    
    @PostConstruct
    public void init() {
        if(employeeRepository.getEmployees().isEmpty()) {
            createInitialAdmin(
                    "yourFirstName",
                    "yourLastName",
                    "yourEmail@admin.com",
                    "yourPassword",
                    "ADMIN");
        }
    }

    private void createInitialAdmin(String firstName, String lastName, String email, String password, String role) {
        employeeService.createEmployee(firstName, lastName, email, password, role);
    }
}
