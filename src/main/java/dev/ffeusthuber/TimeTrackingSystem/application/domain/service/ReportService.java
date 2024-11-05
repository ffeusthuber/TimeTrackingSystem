package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WorkdayDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.TimeEntryRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService implements ReportUseCase {
    private final TimeEntryRepository timeEntryRepository;
    private final WorkdayService workdayService;

    public ReportService(TimeEntryRepository timeEntryRepository, WorkdayService workdayService) {
        this.timeEntryRepository = timeEntryRepository;
        this.workdayService = workdayService;
    }

    @Override
    public List<TimeEntryDTO> displayTimeEntriesOfEmployee(long employeeID, ZoneId zoneId) {
        return this.timeEntryRepository.getTimeEntriesByEmployeeId(employeeID).stream()
                .map(timeEntry -> new TimeEntryDTO(timeEntry,zoneId))
                .toList();
    }

    @Override
    public Optional<WorkdayDTO> displayLatestWorkdayOfEmployee(long employeeID) {
       return this.workdayService.getLatestWorkdayForEmployee(employeeID)
               .map(WorkdayDTO::new);
    }
}
