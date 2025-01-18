package dev.ffeusthuber.TimeTrackingSystem.application.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.EmployeeDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WorkScheduleDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin.DeleteEmployeeResponse;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin.DeleteEmployeeResponseStatus;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin.EmployeeManagementUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.AuthenticationUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeManagementService implements EmployeeManagementUseCase {
    private final EmployeeService employeeService;
    private final WorkdayService workdayService;
    private final AuthenticationUtils authenticationUtils;

    public EmployeeManagementService(EmployeeService employeeService, WorkdayService workdayService, AuthenticationUtils authenticationUtils) {
        this.employeeService = employeeService;
        this.workdayService = workdayService;
        this.authenticationUtils = authenticationUtils;
    }

    @Override
    public Employee createEmployee(String firstname, String lastname, String email, String password, String employeeRole, WorkScheduleDTO workScheduleDTO) {
        EmployeeRole role = EmployeeRole.valueOf(employeeRole);
        WorkSchedule workSchedule = workScheduleDTO.toWorkSchedule();
        return employeeService.createEmployee(firstname, lastname, email, password, role, workSchedule);
    }

    @Override
    public DeleteEmployeeResponse deleteEmployee(long employeeId) {
        if(employeeId != authenticationUtils.getAuthenticatedEmployeeID()) {
            workdayService.deleteAllWorkdaysOfEmployee(employeeId);
            employeeService.deleteEmployee(employeeId);
            return new DeleteEmployeeResponse(DeleteEmployeeResponseStatus.SUCCESS);
        }
        return new DeleteEmployeeResponse(DeleteEmployeeResponseStatus.NOT_ALLOWED);
    }

    @Override
    public EmployeeDTO getEmployeeDetails(long employeeID) {
        Employee employee = employeeService.getEmployeeByID(employeeID);
        return new EmployeeDTO(employee);
    }

    @Override
    public List<EmployeeDTO> getEmployeeList() {
        return employeeService.getAllEmployees().stream()
                              .map(EmployeeDTO::new)
                              .toList();
    }

    @Override
    public WorkScheduleDTO getDefaultWorkSchedule() {
        WorkSchedule defaultWorkSchedule = WorkSchedule.createDefaultWorkSchedule();
        return new WorkScheduleDTO(defaultWorkSchedule);
    }
}
