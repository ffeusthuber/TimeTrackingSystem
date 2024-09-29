package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

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

import java.time.ZoneId;

@Controller
public class TimeEntriesController {


    private final GetTimeEntriesUseCase getTimeEntriesService;
    private final TimeTrackingUseCase timeTrackingService;
    private final EmployeeRepository employeeRepository;


    @Autowired
    public TimeEntriesController(GetTimeEntriesUseCase getTimeEntriesService, TimeTrackingUseCase timeTrackingService, EmployeeRepository employeeRepository) {
        this.getTimeEntriesService = getTimeEntriesService;
        this.timeTrackingService = timeTrackingService;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/time-entries")
    public String displayTimeEntriesForEmployee(Model model, ZoneId zoneId) {
        long employeeID = getAuthenticatedEmployeeID();
        addTimeEntriesToModel(model,employeeID);
        return "timeEntries";
    }

    @PostMapping("/time-entries/clock-in")
    public String clockIn(Model model) {
        long employeeID = getAuthenticatedEmployeeID();
        timeTrackingService.clockIn(employeeID);
        //handle clocking Error
        addTimeEntriesToModel(model,employeeID);
        return "timeEntries";
    }

    @PostMapping("/time-entries/clock-out")
    public String clockOut(Model model) {
        long employeeID = getAuthenticatedEmployeeID();
        timeTrackingService.clockOut(employeeID);
        //handle clocking Error
        addTimeEntriesToModel(model,employeeID);
        return "timeEntries";
    }

    @PostMapping("/time-entries/clock-pause")
    public String clockPause(Model model) {
        long employeeID = getAuthenticatedEmployeeID();
        timeTrackingService.clockPause(employeeID);
        //handle clocking Error
        addTimeEntriesToModel(model,employeeID);
        return "timeEntries";
    }

    private long getAuthenticatedEmployeeID() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return employeeRepository.getEmployeeIDByEmail(email);
    }

    private void addTimeEntriesToModel(Model model, Long employeeID) {
        model.addAttribute("timeEntries", getTimeEntriesService.getTimeEntriesForEmployee(employeeID,ZoneId.of("UTC")));
    }
}
