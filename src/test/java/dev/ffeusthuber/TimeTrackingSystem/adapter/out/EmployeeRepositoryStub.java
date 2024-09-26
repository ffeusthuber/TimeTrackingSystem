package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;

import java.util.ArrayList;
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
        return new EmployeeRepositoryStub(new ArrayList<>());
    }

    public static EmployeeRepository withEmployee(Employee employee) {
        return new EmployeeRepositoryStub(List.of(employee));
    }

    @Override
    public Employee getEmployeeByID(long employeeID) {
        return employees.stream().filter((employee -> employee.getEmployeeID().equals(employeeID)))
                 .findFirst()
                 .orElseThrow();
    }

    @Override
    public void save(Employee employee) {
        employees.add(employee);
    }

    @Override
    public List<Employee> getEmployees() {
        return employees;
    }

    @Override
    public Long getEmployeeIDByEmail(String email) {
        return employees.stream().filter(employee -> employee.getEmail().equals(email))
                 .findFirst()
                 .map(Employee::getEmployeeID)
                 .orElse(1L);
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        return employees.stream().filter(employee -> employee.getEmail().equals(email))
                .findFirst()
                .map(employee -> new Employee(
                        employee.getEmployeeID(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getEmail(),
                        employee.getPassword(),
                        employee.getRole(),
                        employee.getClockState()))
                .orElse(null);
    }
}
