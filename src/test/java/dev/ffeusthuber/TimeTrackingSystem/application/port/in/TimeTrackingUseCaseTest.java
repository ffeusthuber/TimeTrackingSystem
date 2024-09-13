package dev.ffeusthuber.TimeTrackingSystem.application.port.in;


import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.TimeTrackingService;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.TimeEntry;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

public class TimeTrackingUseCaseTest {

        @Test
        void canClockInWithGivenEmployeeID() {
            long employeeID = 1L;
            TimeTrackingUseCase timeTrackingUseCase = new TimeTrackingService();

            TimeEntry timeEntry = timeTrackingUseCase.clockIn(employeeID);

            assertThat(timeEntry.getEmployeeID()).isEqualTo(employeeID);
            assertThat(timeEntry.getType()).isEqualTo(TimeEntryType.CLOCK_IN);
        }

}
