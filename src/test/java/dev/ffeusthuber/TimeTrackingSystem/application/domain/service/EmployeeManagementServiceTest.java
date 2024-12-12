package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.adapter.out.EmployeeRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.adapter.out.WorkdayRepositoryStub;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.EmployeeDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WorkScheduleDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin.EmployeeManagementUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeManagementServiceTest {

    @Test
    void canCreateEmployee() {
        //create Employee endpoint can only be reached with role ADMIN
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withoutEmployees();
        EmployeeManagementUseCase employeeManagementService = createEmployeeManagementService(employeeRepository, WorkdayRepositoryStub.withoutWorkdays());

        employeeManagementService.createEmployee("Jane", "Doe", "j.doe@test-mail.com", "password", "USER", new WorkScheduleDTO(8, 8, 8, 8, 8, 0, 0));

        assertThat(employeeRepository.getAllEmployees()).isNotNull();
    }

    @Test
    void whenDeletingEmployeeAlsoAllCorrespondingWorkdaysGetDeleted() {
        long employeeId = 1L;
        EmployeeRepository employeeRepository = EmployeeRepositoryStub.withEmployee(createEmployee(employeeId));
        Workday workdayOfEmployee = new Workday(employeeId, LocalDate.now(), 8);
        Workday workdayOfOtherEmployee = new Workday(employeeId + 1, LocalDate.now(), 8);
        WorkdayRepositoryStub workdayRepository = WorkdayRepositoryStub.withWorkdays(workdayOfEmployee, workdayOfOtherEmployee);
        EmployeeManagementService employeeManagementService = createEmployeeManagementService(employeeRepository, workdayRepository);

        employeeManagementService.deleteEmployee(employeeId);

        assertThat(employeeRepository.getAllEmployees()).isEmpty();
        assertThat(workdayRepository.getAllWorkdaysOfEmployee(employeeId)).isEmpty();
    }

    @Test
    void canGetEmployeeDetailsById() {
        EmployeeRepository employeeRepositoryStub = EmployeeRepositoryStub.withEmployee(createEmployee());
        EmployeeManagementUseCase employeeManagementService = createEmployeeManagementService(employeeRepositoryStub, WorkdayRepositoryStub.withoutWorkdays());

        EmployeeDTO employeeDTO = employeeManagementService.getEmployeeDetails(1L);

        assertThat(employeeDTO.fullName()).isEqualTo("Jane Doe");
        assertThat(employeeDTO.role()).isEqualTo("USER");
    }

    @Test
    void canGetDefaultWorkSchedule() {
        EmployeeService employeeService = new EmployeeService(EmployeeRepositoryStub.withoutEmployees());
        EmployeeManagementUseCase employeeManagementService = new EmployeeManagementService(employeeService, new WorkdayService(employeeService, WorkdayRepositoryStub.withoutWorkdays()));

        WorkScheduleDTO workScheduleDTO = employeeManagementService.getDefaultWorkSchedule();

        assertThat(workScheduleDTO).isEqualTo(new WorkScheduleDTO(WorkSchedule.createDefaultWorkSchedule()));
    }

    @Test
    void canGetEmployeeList(){
        Employee employee = createEmployee();
        EmployeeRepository employeeRepositoryStub = EmployeeRepositoryStub.withEmployee(employee);
        EmployeeManagementUseCase employeeManagementService = createEmployeeManagementService(employeeRepositoryStub, WorkdayRepositoryStub.withoutWorkdays());
        List<EmployeeDTO> expectedList = List.of(new EmployeeDTO(employee));

        List<EmployeeDTO> actualList = employeeManagementService.getEmployeeList();

        assertThat(actualList).isEqualTo(expectedList);
    }

    private static Employee createEmployee(long id) {
        return new Employee(id, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());
    }

    private static Employee createEmployee() {
        return createEmployee(1L);
    }

    private static EmployeeManagementService createEmployeeManagementService(EmployeeRepository employeeRepository, WorkdayRepository workdayRepository) {
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        WorkdayService workdayService = new WorkdayService(employeeService, workdayRepository);
        return new EmployeeManagementService(employeeService, workdayService);
    }
}
