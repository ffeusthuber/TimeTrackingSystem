package dev.ffeusthuber.TimeTrackingSystem.util;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("io")
public class AuthenticationUtilsTest {

    @BeforeEach
    void setUp() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void testGetAuthenticatedEmployeeID() {
        long employeeID = 1L;
        Employee employee = new Employee(employeeID, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());
        EmployeeRepository employeeRepositoryStub = EmployeeRepositoryStub.withEmployee(employee);
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("j.doe@test-mail.com");
        AuthenticationUtils authenticationUtils = new AuthenticationUtils(employeeRepositoryStub);

        assertThat(authenticationUtils.getAuthenticatedEmployeeID()).isEqualTo(employeeID);
    }
}
