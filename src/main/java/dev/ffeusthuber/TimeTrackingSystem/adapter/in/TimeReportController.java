package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;
import dev.ffeusthuber.TimeTrackingSystem.util.AuthenticationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TimeReportController {

    private final ReportUseCase reportUseCase;
    private final AuthenticationUtils authenticationUtils;

    public TimeReportController(ReportUseCase reportUseCase, AuthenticationUtils authenticationUtils) {
        this.reportUseCase = reportUseCase;
        this.authenticationUtils = authenticationUtils;
    }

    @GetMapping("/time-report")
    public String displayLatestWorkday(Model model) {
        long employeeID = authenticationUtils.getAuthenticatedEmployeeID();
        model.addAttribute("workday", reportUseCase.getLatestWorkdayOfEmployee(employeeID));
        return "timeReport";
    }
}
