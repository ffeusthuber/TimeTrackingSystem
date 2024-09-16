package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.TimeEntryRepository;

import java.util.Optional;

public class EmployeeClockStatusService {
    private final TimeEntryRepository timeEntryRepository;

    public EmployeeClockStatusService(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    public TimeEntryType checkEmployeeClockStatus(long employeeID) {
        return Optional.ofNullable(timeEntryRepository.getCurrentTimeEntryForEmployee(employeeID))
                       .map((TimeEntry::getType))
                       .orElse(TimeEntryType.CLOCK_OUT);
    }
}
