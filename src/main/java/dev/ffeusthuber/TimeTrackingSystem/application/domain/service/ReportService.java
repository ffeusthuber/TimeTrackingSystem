package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.dto.TimeEntryDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.TimeEntryRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;

@Service
public class ReportService implements ReportUseCase {
    private final TimeEntryRepository timeEntryRepository;

    public ReportService(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @Override
    public List<TimeEntryDTO> displayTimeEntriesOfEmployee(long employeeID, ZoneId zoneId) {
        return this.timeEntryRepository.getTimeEntriesByEmployeeId(employeeID).stream()
                .map(timeEntry -> new TimeEntryDTO(timeEntry,zoneId))
                .toList();
    }
}
