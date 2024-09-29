package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryDTO;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.GetTimeEntriesUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.TimeEntryRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;

@Service
public class GetTimeEntriesService implements GetTimeEntriesUseCase {
    private final TimeEntryRepository timeEntryRepository;

    public GetTimeEntriesService(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @Override
    public List<TimeEntryDTO> getTimeEntriesForEmployee(long employeeID, ZoneId zoneId) {
        return this.timeEntryRepository.getTimeEntriesByEmployeeId(employeeID).stream()
                .map(timeEntry -> new TimeEntryDTO(timeEntry,zoneId))
                .toList();
    }
}
