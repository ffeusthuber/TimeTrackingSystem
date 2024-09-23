package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeService;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeControllerTest {

        @Test
        void successfullyCreatedEmployeeIsAddedToEmployeeRepository() {
            EmployeeRepository employeeRepositoryStub = EmployeeRepositoryStub.withoutEmployees();

            EmployeeController employeeController = new EmployeeController(new EmployeeService(employeeRepositoryStub));

            employeeController.createEmployee("Jane", "Doe", "j.doe@test-mail.com", "password", "USER");

            assertThat(employeeRepositoryStub.getEmployees().size()).isEqualTo(1);
        }
}
