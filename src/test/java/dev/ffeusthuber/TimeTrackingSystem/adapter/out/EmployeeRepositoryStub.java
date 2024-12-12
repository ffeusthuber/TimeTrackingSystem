package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeRepositoryStub implements EmployeeRepository {
    List<Employee> employees;

    private EmployeeRepositoryStub(List<Employee> employees) {
        this.employees = employees;
    }

    public static EmployeeRepositoryStub withEmployees(List<Employee> employees) {
        return new EmployeeRepositoryStub(new ArrayList<>(employees));
    }

    public static EmployeeRepository withoutEmployees() {
        return new EmployeeRepositoryStub(new ArrayList<>());
    }

    public static EmployeeRepository withEmployee(Employee employee) {
        return new EmployeeRepositoryStub(new ArrayList<>(List.of(employee)));
    }

    @Override
    public Optional<Employee> getEmployeeByID(long employeeID) {
        return Optional.of(employees.stream().filter((employee -> employee.getEmployeeID().equals(employeeID)))
                                    .findFirst()
                                    .orElseThrow());
    }

    @Override
    public Employee save(Employee employee) {
        employees.add(employee);
        employee.setEmployeeID((long) employees.indexOf(employee));
        return employee;
    }

    @Override
    public void delete(long employeeId) {
        employees.removeIf(employee -> employee.getEmployeeID().equals(employeeId));
    }

    @Override
    public void setClockStateForEmployee(Long employeeID, String clockState) {
        Employee employee = employees.stream().filter(employee1 -> employee1.getEmployeeID().equals(employeeID))
                 .findFirst()
                 .orElseThrow();

        switch(clockState) {
            case "CLOCKED_IN":
                employee.clockIn();
                break;
            case "CLOCKED_OUT":
                employee.clockOut();
                break;
            case "ON_PAUSE":
                employee.clockPause();
                break;
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
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
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employees.stream().filter(employee -> employee.getEmail().equals(email))
                        .findFirst()
                        .map(employee -> new Employee(
                                                    employee.getEmployeeID(),
                                                    employee.getFirstName(),
                                                    employee.getLastName(),
                                                    employee.getEmail(),
                                                    employee.getPassword(),
                                                    employee.getRole(),
                                                    employee.getClockState(),
                                                    employee.getWorkSchedule()));
    }

    @Override
    public void updatePasswordForEmployee(long employeeId, String encryptedPassword) {
        Employee employee = employees.stream().filter(employee1 -> employee1.getEmployeeID().equals(employeeId))
                 .findFirst()
                 .orElseThrow();
        employee.setPassword(encryptedPassword);
    }
}
