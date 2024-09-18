package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.TimeTrackingSystemController;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.GetTimeEntriesServiceStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeTrackingSystemControllerTest {

    private long employeeID;
    private List<TimeEntry> timeEntries;
    private TimeTrackingSystemController timeTrackingSystemController;

    @BeforeEach
    void setUp() {
        employeeID = 1L;

        TimeEntry timeEntry1 = new TimeEntry(employeeID, TimeEntryType.CLOCK_IN, ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
        TimeEntry timeEntry2 = new  TimeEntry(employeeID, TimeEntryType.CLOCK_OUT, ZonedDateTime.of(2021, 1, 1, 8, 0, 0, 0, ZoneOffset.UTC));
        timeEntries = List.of(timeEntry1, timeEntry2);

        GetTimeEntriesServiceStub getTimeEntriesServiceStub = GetTimeEntriesServiceStub.withTimeEntriesToReturn(timeEntries);
        timeTrackingSystemController = new TimeTrackingSystemController(getTimeEntriesServiceStub);
    }

    @Test
    void shouldAddTimeEntriesToModelWhenEmployeeIdIsPresent() {
        Model model = new ExtendedModelMap();
        model.addAttribute("employeeID", employeeID);

        timeTrackingSystemController.timeEntries(model);

        assertThat(model.getAttribute("timeEntries")).isEqualTo(timeEntries);
    }
}
