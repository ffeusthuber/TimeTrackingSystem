package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.AuthenticationUtilsStub;
import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.adapter.out.WorkdayRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeManagementService;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeService;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.WorkdayService;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin.EmployeeManagementUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalControllerAdviceTest {

    @Test
    void fullNameAndRoleAreAddedToModelForDisplayInSidebar() {
        long employeeID = 1L;
        Employee employee = new Employee(employeeID, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withEmployee(employee);
        WorkdayRepository workdayRepository = WorkdayRepositoryStub.withoutWorkdays();
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        WorkdayService workdayService = new WorkdayService(employeeService, workdayRepository);
        EmployeeManagementUseCase employeeManagementService = new EmployeeManagementService(employeeService, workdayService, new AuthenticationUtilsStub(employeeID));
        GlobalControllerAdvice globalControllerAdvice = new GlobalControllerAdvice(employeeManagementService, new AuthenticationUtilsStub(employeeID));
        Model model = new ExtendedModelMap();

        globalControllerAdvice.addAttributesForSidebar(model);

        assertThat(model.getAttribute("fullName")).isEqualTo("Jane Doe");
        assertThat(model.getAttribute("role")).isEqualTo("USER");
    }
}
