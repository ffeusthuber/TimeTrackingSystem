package dev.ffeusthuber.TimeTrackingSystem.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TimeTrackingSystemController {

    @GetMapping("/")
    public String homePage() {
        return "index.html";
    }
}
