package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin.EmployeeManagementUseCase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EmployeeManagementController {

    private final EmployeeManagementUseCase employeeManagementService;

    public EmployeeManagementController(EmployeeManagementUseCase employeeManagementService) {
        this.employeeManagementService = employeeManagementService;
    }

    @GetMapping("/create-employee")
    public String createEmployee() {
        return "createEmployee";
    }

    @PostMapping("/create-employee")
    public String createEmployee(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role,
            RedirectAttributes redirectAttributes) {

        try {
            employeeManagementService.createEmployee(firstName, lastName, email, password, role);
        } catch (DuplicateKeyException e) {
            redirectAttributes.addFlashAttribute("message", "Email already in use!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-failure");
            return "redirect:/create-employee?error";
        }
        redirectAttributes.addFlashAttribute("message", "Employee created successfully!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/create-employee?success";
    }
}
