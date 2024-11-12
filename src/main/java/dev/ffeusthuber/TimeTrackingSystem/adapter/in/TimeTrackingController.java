package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockResponse;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockResponseStatus;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.TimeTrackingUseCase;
import dev.ffeusthuber.TimeTrackingSystem.util.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.ZoneId;

@Controller
public class TimeTrackingController {


    private final ReportUseCase reportService;
    private final TimeTrackingUseCase timeTrackingService;
    private final AuthenticationUtils authenticationUtils;
    private ZoneId zoneId;


    @Autowired
    public TimeTrackingController(ReportUseCase reportUseCase, TimeTrackingUseCase timeTrackingUseCase, AuthenticationUtils authenticationUtils) {
        this.reportService = reportUseCase;
        this.timeTrackingService = timeTrackingUseCase;
        this.authenticationUtils = authenticationUtils;
    }


    @GetMapping({"/time-tracking","/"})
    public String displayTimeEntriesOfLatestWorkdayOfEmployee(Model model, ZoneId zoneId) {
        long employeeID = authenticationUtils.getAuthenticatedEmployeeID();
        this.zoneId = zoneId;
        model.addAttribute("timeEntries", reportService.getTimeEntriesOfLatestWorkdayOfEmployee(employeeID, this.zoneId));
        return "timeTracking";
    }

    @PostMapping("/time-tracking/clock-in")
    public String clockIn(RedirectAttributes redirectAttributes) {
        long employeeID = authenticationUtils.getAuthenticatedEmployeeID();
        ClockResponse clockResponse = timeTrackingService.clockIn(employeeID);
        return processClockAction(redirectAttributes, clockResponse, employeeID);
    }

    @PostMapping("/time-tracking/clock-out")
    public String clockOut(RedirectAttributes redirectAttributes) {
        long employeeID = authenticationUtils.getAuthenticatedEmployeeID();
        ClockResponse clockResponse = timeTrackingService.clockOut(employeeID);
        return processClockAction(redirectAttributes, clockResponse, employeeID);
    }

    @PostMapping("/time-tracking/clock-pause")
    public String clockPause(RedirectAttributes redirectAttributes) {
        long employeeID = authenticationUtils.getAuthenticatedEmployeeID();
        ClockResponse clockResponse = timeTrackingService.clockPause(employeeID);
        return processClockAction(redirectAttributes, clockResponse, employeeID);
    }

    private String processClockAction(RedirectAttributes redirectAttributes, ClockResponse clockResponse, Long employeeID) {
        addTimeEntriesToFlashAttributes(redirectAttributes, employeeID);

        if(clockResponse.getStatus() == ClockResponseStatus.SUCCESS){
            setSuccessFlashAttributes(redirectAttributes, clockResponse);
            return "redirect:/time-tracking?success";
        } else {
            setErrorFlashAttributes(redirectAttributes, clockResponse);
        }
        return "redirect:/time-tracking?error";
    }

    private void addTimeEntriesToFlashAttributes(RedirectAttributes redirectAttributes, Long employeeID) {
        redirectAttributes.addFlashAttribute("timeEntries", reportService.getTimeEntriesOfLatestWorkdayOfEmployee(employeeID, zoneId));
    }

    private void setSuccessFlashAttributes(RedirectAttributes redirectAttributes, ClockResponse clockResponse) {
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        switch (clockResponse.getTimEntryType()) {
            case CLOCK_IN:
                redirectAttributes.addFlashAttribute("message", "You successfully clocked in!");
                break;
            case CLOCK_OUT:
                redirectAttributes.addFlashAttribute("message", "You successfully clocked out!");
                break;
            case CLOCK_PAUSE:
                redirectAttributes.addFlashAttribute("message", "You successfully clocked pause!");
                break;
        }
    }

    private void setErrorFlashAttributes(RedirectAttributes redirectAttributes, ClockResponse clockResponse) {
        redirectAttributes.addFlashAttribute("alertClass", "alert-failure");
        switch (clockResponse.getClockError()) {
            case EMPLOYEE_ALREADY_CLOCKED_IN:
                redirectAttributes.addFlashAttribute("message", "You are already clocked in!");
                break;
            case EMPLOYEE_NOT_CLOCKED_IN:
                redirectAttributes.addFlashAttribute("message", "You are not clocked in!");
                break;
        }
    }
}
