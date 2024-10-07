package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockResponse;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.ClockResponseStatus;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.GetTimeEntriesUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.TimeTrackingUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.ZoneId;

@Controller
public class TimeEntriesController {


    private final GetTimeEntriesUseCase getTimeEntriesService;
    private final TimeTrackingUseCase timeTrackingService;
    private final EmployeeRepository employeeRepository;
    private ZoneId zoneId;


    @Autowired
    public TimeEntriesController(GetTimeEntriesUseCase getTimeEntriesService, TimeTrackingUseCase timeTrackingService, EmployeeRepository employeeRepository) {
        this.getTimeEntriesService = getTimeEntriesService;
        this.timeTrackingService = timeTrackingService;
        this.employeeRepository = employeeRepository;
    }


    @GetMapping({"/time-entries","/"})
    public String displayTimeEntriesForEmployee(Model model, ZoneId zoneId) {
        long employeeID = getAuthenticatedEmployeeID();
        this.zoneId = zoneId;
        addModelAttributes(model, employeeID);
        return "timeEntries";
    }

    @PostMapping("/time-entries/clock-in")
    public String clockIn(RedirectAttributes redirectAttributes) {
        long employeeID = getAuthenticatedEmployeeID();
        ClockResponse clockResponse = timeTrackingService.clockIn(employeeID);
        return processClockAction(redirectAttributes, clockResponse, employeeID);
    }

    @PostMapping("/time-entries/clock-out")
    public String clockOut(RedirectAttributes redirectAttributes) {
        long employeeID = getAuthenticatedEmployeeID();
        ClockResponse clockResponse = timeTrackingService.clockOut(employeeID);
        return processClockAction(redirectAttributes, clockResponse, employeeID);
    }

    @PostMapping("/time-entries/clock-pause")
    public String clockPause(RedirectAttributes redirectAttributes) {
        long employeeID = getAuthenticatedEmployeeID();
        ClockResponse clockResponse = timeTrackingService.clockPause(employeeID);
        return processClockAction(redirectAttributes, clockResponse, employeeID);
    }

    private long getAuthenticatedEmployeeID() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return employeeRepository.getEmployeeIDByEmail(email);
    }

    private void addModelAttributes(Model model, long employeeID) {
        model.addAttribute("timeEntries", getTimeEntriesService.getTimeEntriesForEmployee(employeeID, zoneId));
    }

    private String processClockAction(RedirectAttributes redirectAttributes, ClockResponse clockResponse, Long employeeID) {
        addTimeEntriesToFlashAttributes(redirectAttributes, employeeID);

        if(clockResponse.getStatus() == ClockResponseStatus.SUCCESS){
            setSuccessFlashAttributes(redirectAttributes, clockResponse);
            return "redirect:/time-entries?success";
        } else {
            setErrorFlashAttributes(redirectAttributes, clockResponse);
        }
        return "redirect:/time-entries?error";
    }

    private void addTimeEntriesToFlashAttributes(RedirectAttributes redirectAttributes, Long employeeID) {
        redirectAttributes.addFlashAttribute("timeEntries", getTimeEntriesService.getTimeEntriesForEmployee(employeeID, zoneId));
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
