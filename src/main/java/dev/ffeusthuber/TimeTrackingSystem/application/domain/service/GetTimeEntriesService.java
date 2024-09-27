package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.GetTimeEntriesUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.TimeEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetTimeEntriesService implements GetTimeEntriesUseCase {
    private final TimeEntryRepository timeEntryRepository;

    public GetTimeEntriesService(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @Override
    public List<TimeEntry> getTimeEntriesForEmployee(long employeeID) {
        return this.timeEntryRepository.getTimeEntriesByEmployeeId(employeeID);
    }
}
