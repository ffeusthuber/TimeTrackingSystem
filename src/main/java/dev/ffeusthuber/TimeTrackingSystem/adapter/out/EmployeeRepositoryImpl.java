package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class EmployeeRepositoryImpl implements EmployeeRepository {
    @Override
    public Employee getEmployeeById(long employeeID) {
        return null;
    }

    @Override
    public void save(Employee employee) {

    }

    @Override
    public List<Employee> getEmployees() {
        return List.of();
    }
}
