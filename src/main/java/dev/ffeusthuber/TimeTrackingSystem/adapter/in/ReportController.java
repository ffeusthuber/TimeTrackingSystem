package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.AuthenticationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

    private final ReportUseCase reportUseCase;
    private final AuthenticationUtils authenticationUtils;

    public ReportController(ReportUseCase reportUseCase, AuthenticationUtils authenticationUtils) {
        this.reportUseCase = reportUseCase;
        this.authenticationUtils = authenticationUtils;
    }

    @GetMapping("/time-report")
    public String displayReportForCurrentWeek(Model model) {
        long employeeID = authenticationUtils.getAuthenticatedEmployeeID();
        model.addAttribute("weekReport", reportUseCase.getCurrentWeekReportForEmployee(employeeID));

        return "timeReport";
    }
}
