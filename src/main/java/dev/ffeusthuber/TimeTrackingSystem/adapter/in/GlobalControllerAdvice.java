package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.EmployeeDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin.EmployeeManagementUseCase;
import dev.ffeusthuber.TimeTrackingSystem.util.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final EmployeeManagementUseCase employeeManagementService;
    private final AuthenticationUtils authenticationUtils;

    @Autowired
    public GlobalControllerAdvice(EmployeeManagementUseCase employeeManagementService, AuthenticationUtils authenticationUtils) {
        this.employeeManagementService = employeeManagementService;
        this.authenticationUtils = authenticationUtils;
    }

    @ModelAttribute
    public void addAttributesForSidebar(Model model) {
        long employeeID = authenticationUtils.getAuthenticatedEmployeeID();
        EmployeeDTO employeeDTO = employeeManagementService.getEmployee(employeeID);
        if (employeeDTO != null) {
            model.addAttribute("fullName", employeeDTO.getFullName());
            model.addAttribute("role", employeeDTO.getRole());
        }
    }
}