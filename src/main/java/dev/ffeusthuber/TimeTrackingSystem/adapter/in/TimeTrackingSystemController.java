package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.GetTimeEntriesUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TimeTrackingSystemController {


    private final GetTimeEntriesUseCase getTimeEntriesService;
    private final EmployeeRepository employeeRepository;


    @Autowired
    public TimeTrackingSystemController(GetTimeEntriesUseCase getTimeEntriesService, EmployeeRepository employeeRepository) {
        this.getTimeEntriesService = getTimeEntriesService;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping({"/","/home"})
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/time-entries")
    public String displayTimeEntriesForEmployee(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        long employeeID = employeeRepository.getEmployeeIDByEmail(email);
        model.addAttribute("timeEntries", getTimeEntriesService.getTimeEntriesForEmployee(employeeID));
        return "timeEntries";
    }

}
