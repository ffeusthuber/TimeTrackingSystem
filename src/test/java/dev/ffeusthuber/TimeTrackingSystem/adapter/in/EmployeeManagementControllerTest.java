package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.AuthenticationUtilsStub;
import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.adapter.out.WorkdayRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin.EmployeeManagementUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import dev.ffeusthuber.TimeTrackingSystem.application.service.EmployeeManagementService;
import dev.ffeusthuber.TimeTrackingSystem.application.service.EmployeeService;
import dev.ffeusthuber.TimeTrackingSystem.application.service.WorkdayService;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeManagementControllerTest {

    @Test
    void successfullyCreatedEmployeeIsAddedToEmployeeRepository() {
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withoutEmployees();
        EmployeeManagementController employeeManagementController = createEmployeeManagementController(employeeRepository);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        float[] dailyWorkHours = {8,8,8,8,8,0,0};

        employeeManagementController.createEmployee("Jane", "Doe", "j.doe@test-mail.com", "password", "USER", dailyWorkHours,redirectAttributes);

        assertThat(employeeRepository.getAllEmployees().size()).isEqualTo(1);
    }

    private EmployeeManagementController createEmployeeManagementController(EmployeeRepository employeeRepository) {
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        WorkdayService workdayService = new WorkdayService(employeeService, WorkdayRepositoryStub.withoutWorkdays());
        EmployeeManagementUseCase employeeManagementService = new EmployeeManagementService(employeeService, workdayService, new AuthenticationUtilsStub(1L));
        return new EmployeeManagementController(employeeManagementService);
    }

}
