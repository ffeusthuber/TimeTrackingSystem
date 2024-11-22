package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.ChangePasswordResponse;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ChangePasswordUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.AuthenticationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SettingsController {

    private final ChangePasswordUseCase changePasswordUseCase;
    private final AuthenticationUtils authenticationUtils;

    public SettingsController(ChangePasswordUseCase changePasswordUseCase, AuthenticationUtils authenticationUtils) {
        this.changePasswordUseCase = changePasswordUseCase;
        this.authenticationUtils = authenticationUtils;
    }

    @GetMapping("/change-password")
    public String displayChangePasswordForm() {
        return "changePassword";
    }

    @PostMapping("/change-password")
    public String changePasswordPost(@RequestParam String currentPassword,
                                     @RequestParam String newPassword,
                                     @RequestParam String confirmNewPassword,
                                     RedirectAttributes redirectAttributes) {
        if(!newPassword.equals(confirmNewPassword)) {
            redirectAttributes.addFlashAttribute("alertClass", "alert-failure");
            redirectAttributes.addFlashAttribute("message", "New password does not match");
            return "redirect:/change-password?error";
        }
        
        ChangePasswordResponse changePasswordResponse = changePasswordUseCase.changePasswordForEmployee(authenticationUtils.getAuthenticatedEmployeeID(),
                                                                                                        currentPassword,
                                                                                                        newPassword);

        switch (changePasswordResponse.status()) {
            case WRONG_PASSWORD:
                redirectAttributes.addFlashAttribute("alertClass", "alert-failure");
                redirectAttributes.addFlashAttribute("message", "Entered current password is incorrect");
                return "redirect:/change-password?error";
            case SUCCESS:
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
                redirectAttributes.addFlashAttribute("message", "Password changed successfully");
                return "redirect:/change-password?success";
            default:
                redirectAttributes.addFlashAttribute("alertClass", "alert-failure");
                redirectAttributes.addFlashAttribute("message", "An unknown error occurred");
                return "redirect:/change-password?error";
        }
    }
}
