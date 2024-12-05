package dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.EmployeeDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WorkScheduleDTO;

import java.util.List;

public interface EmployeeManagementUseCase {
    Employee createEmployee(String firstname, String lastname, String email, String password, String employeeRole, WorkScheduleDTO workSchedule);
    EmployeeDTO getEmployeeDetails(long employeeID);
    List<EmployeeDTO> getEmployeeList();
    WorkScheduleDTO getDefaultWorkSchedule();
}
