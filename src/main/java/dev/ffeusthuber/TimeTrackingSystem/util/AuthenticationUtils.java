package dev.ffeusthuber.TimeTrackingSystem.util;

import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtils {

    private final EmployeeRepository employeeRepository;

    public AuthenticationUtils(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public long getAuthenticatedEmployeeID() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return employeeRepository.getEmployeeIDByEmail(email);
    }
}