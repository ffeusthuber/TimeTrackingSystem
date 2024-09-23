package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.GetTimeEntriesUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TimeTrackingSystemController {

    GetTimeEntriesUseCase getTimeEntriesService;

    @Autowired
    public TimeTrackingSystemController(GetTimeEntriesUseCase getTimeEntriesService) {
        this.getTimeEntriesService = getTimeEntriesService;
    }

    @GetMapping({"/","/home"})
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/time-entries")
    public String displayTimeEntriesForEmployee(Model model) {
        long employeeID = (long) model.getAttribute("employeeID");
        model.addAttribute("timeEntries", getTimeEntriesService.getTimeEntriesForEmployee(employeeID));
        return "timeEntries";
    }

}
