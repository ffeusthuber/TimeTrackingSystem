package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;

public class AuthenticationUtilsStub extends SpringAuthenticationUtils {
    private final long employeeIDToReturn;

    public AuthenticationUtilsStub(EmployeeRepository employeeRepository, long employeeIDToReturn) {
        super(employeeRepository);
        this.employeeIDToReturn = employeeIDToReturn;
    }

    @Override
    public long getAuthenticatedEmployeeID() {
        return this.employeeIDToReturn;
    }
}
