package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.dto.WorkScheduleDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.admin.EmployeeManagementUseCase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String displayEmployeeForm(Model model) {
        model.addAttribute("defaultWorkSchedule", employeeManagementService.getDefaultWorkSchedule());
        return "createEmployee";
    }

    @PostMapping("/create-employee")
    public String createEmployee(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role,
            @RequestParam float[] dailyWorkHours, // starting from Monday
            RedirectAttributes redirectAttributes) {

        try {
            WorkScheduleDTO workScheduleDTO = parseToWorkScheduleDTO(dailyWorkHours);
            employeeManagementService.createEmployee(firstName, lastName, email, password, role, workScheduleDTO);

            addRedirectAttributes(redirectAttributes, "Employee created successfully!", "alert-success");
            return "redirect:/create-employee?success";

        } catch (DuplicateKeyException e) {
            addRedirectAttributes(redirectAttributes, "Email already in use!", "alert-failure");
            return "redirect:/create-employee?error";
        }
    }

    @GetMapping("/access-denied")
    public String displayAccessDenied() {
        return "accessDenied";
    }

    private WorkScheduleDTO parseToWorkScheduleDTO(float[] dailyWorkHours) {
        return new WorkScheduleDTO(dailyWorkHours[0],
                                   dailyWorkHours[1],
                                   dailyWorkHours[2],
                                   dailyWorkHours[3],
                                   dailyWorkHours[4],
                                   dailyWorkHours[5],
                                   dailyWorkHours[6]);
    }

    private void addRedirectAttributes(RedirectAttributes redirectAttributes, String message, String alertClass) {
        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("alertClass", alertClass);
    }
}
