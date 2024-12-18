package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.port.out.AuthenticationUtils;

public class AuthenticationUtilsStub implements AuthenticationUtils {
    private final long employeeIDToReturn;

    public AuthenticationUtilsStub(long employeeIDToReturn) {
        this.employeeIDToReturn = employeeIDToReturn;
    }

    @Override
    public long getAuthenticatedEmployeeID() {
        return this.employeeIDToReturn;
    }
}
