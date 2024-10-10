package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TimeReportController {

    @GetMapping("/time-report")
    public String displayTimeReport() {
        return "timeReport";
    }
}
