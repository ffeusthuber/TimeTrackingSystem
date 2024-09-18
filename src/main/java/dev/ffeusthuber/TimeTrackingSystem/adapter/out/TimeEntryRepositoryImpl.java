package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.TimeEntryRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class TimeEntryRepositoryImpl implements TimeEntryRepository {
    @Override
    public TimeEntry getLastEntryByEmployeeId(long employeeID) {
        return null;
    }

    @Override
    public List<TimeEntry> getTimeEntriesByEmployeeId(long employeeID) {
        return List.of();
    }

    @Override
    public void save(TimeEntry timeEntry) {

    }
}
