package dev.ffeusthuber.TimeTrackingSystem.application.port.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;

import java.util.List;

public class EmployeeRepositoryStub implements EmployeeRepository {
    List<Employee> employees;

    private EmployeeRepositoryStub(List<Employee> employees) {
        this.employees = employees;
    }

    public static EmployeeRepositoryStub withEmployees(List<Employee> employees) {
        return new EmployeeRepositoryStub(employees);
    }

    public static EmployeeRepository withoutEmployees() {
        return new EmployeeRepositoryStub(List.of());
    }

    public static EmployeeRepository withEmployee(Employee employee) {
        return new EmployeeRepositoryStub(List.of(employee));
    }

    @Override
    public Employee getEmployeeById(long employeeID) {
        return employees.stream().filter((employee -> employee.getEmployeeID().equals(employeeID)))
                 .findFirst()
                 .orElseThrow();
    }
}
