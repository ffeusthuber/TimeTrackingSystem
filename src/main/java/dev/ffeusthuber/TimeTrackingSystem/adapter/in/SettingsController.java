package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingsController {

    @GetMapping("/change-password")
    public String changePassword() {
        return "changePassword";
    }
}
