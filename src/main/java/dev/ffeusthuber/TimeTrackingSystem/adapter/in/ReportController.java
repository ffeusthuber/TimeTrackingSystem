package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.AuthenticationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

@Controller
public class ReportController {

    private final ReportUseCase reportUseCase;
    private final AuthenticationUtils authenticationUtils;

    public ReportController(ReportUseCase reportUseCase, AuthenticationUtils authenticationUtils) {
        this.reportUseCase = reportUseCase;
        this.authenticationUtils = authenticationUtils;
    }

    @GetMapping("/time-report")
    public String displayReportForWeek(Model model,
                                       @RequestParam(required = false, name = "weekNumber") Integer weekNumber,
                                       @RequestParam(required = false, name = "year") Integer year) {
        long employeeID = authenticationUtils.getAuthenticatedEmployeeID();
        populateModelWithWeekReport(model, employeeID, weekNumber, year);

        return "timeReport";
    }

    @GetMapping("/employees/time-report")
    public String displayReportForEmployeeAndWeek(Model model,
                                                  @RequestParam long employeeID,
                                                  @RequestParam(required = false, name = "weekNumber") Integer weekNumber,
                                                  @RequestParam(required = false, name = "year") Integer year) {
        populateModelWithWeekReport(model, employeeID, weekNumber, year);

        return "timeReport";
    }

    private void populateModelWithWeekReport(Model model, long employeeID, Integer weekNumber, Integer year) {
        int currentYear = LocalDate.now().get(WeekFields.ISO.weekBasedYear());
        int currentWeekNumber = LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear());

        year = (year == null) ? currentYear : year;
        weekNumber = (weekNumber == null) ? currentWeekNumber : weekNumber;

        model.addAttribute("weekReport", reportUseCase.getWeekReportForEmployeeAndWeekOfYear(employeeID, weekNumber, year));
    }


}
