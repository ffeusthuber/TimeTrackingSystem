package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockError;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.ClockResponse;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.timeEntry.TimeEntryType;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeManagementService;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.TimeTrackingUseCase;
import dev.ffeusthuber.TimeTrackingSystem.config.SecurityConfiguration;
import dev.ffeusthuber.TimeTrackingSystem.util.AuthenticationUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TimeTrackingController.class)
@Import(SecurityConfiguration.class)
@Tag("io")
public class TimeEntriesMvcTest {

    @MockBean
    TimeTrackingUseCase timeTrackingUseCase;

    @MockBean
    ReportUseCase reportUseCase;

    @MockBean
    EmployeeManagementService employeeManagementService;

    @MockBean
    AuthenticationUtils authenticationUtils;

    @Autowired
    MockMvc mockMvc;


    @Test
    void whenGetToTimeEntriesWithOutLoggedInUserRedirectToLogin() throws Exception {
        mockMvc.perform(get("/time-tracking"))
               .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    void whenGetToHomeReturnTimeEntriesView() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("timeTracking"));
    }

    @Test
    @WithMockUser
    void whenGetTimeEntriesReturnViewWithTimeEntriesInModel() throws Exception {
        mockMvc.perform(get("/time-tracking"))
               .andExpect(model().attributeExists("timeEntries"));
    }

    @Test
    @WithMockUser
    void whenPostToClockInEmployeeThenClockInEmployee() throws Exception {
        long employeeID = 0L;
        ClockResponse clockResponse = ClockResponse.success(employeeID, TimeEntryType.CLOCK_IN);
        when(timeTrackingUseCase.clockIn(employeeID)).thenReturn(clockResponse);

        mockMvc.perform(post("/time-tracking/clock-in")
                                .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(flash().attributeExists("timeEntries"))
               .andExpect(redirectedUrl("/time-tracking?success"))
               .andExpect(flash().attribute("alertClass", "alert-success"))
               .andExpect(flash().attributeExists("message"));
    }

    @Test
    @WithMockUser
    void whenUnsuccessfullyClockingInDisplayWarning() throws Exception {
        long employeeID = 0L;
        ClockResponse clockResponse = ClockResponse.error(employeeID, ClockError.EMPLOYEE_ALREADY_CLOCKED_IN);
        when(timeTrackingUseCase.clockIn(employeeID)).thenReturn(clockResponse);

        mockMvc.perform(post("/time-tracking/clock-in")
                                .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(flash().attributeExists("timeEntries"))
               .andExpect(redirectedUrl("/time-tracking?error"))
               .andExpect(flash().attribute("alertClass", "alert-failure"))
               .andExpect(flash().attributeExists("message"));
    }

    @Test
    @WithMockUser
    void whenPostToClockOutEmployeeThenClockOutEmployee() throws Exception {
        long employeeID = 0L;
        ClockResponse clockResponse = ClockResponse.success(employeeID, TimeEntryType.CLOCK_OUT);
        when(timeTrackingUseCase.clockOut(employeeID)).thenReturn(clockResponse);
        mockMvc.perform(post("/time-tracking/clock-out")
                                .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(flash().attributeExists("timeEntries"))
               .andExpect(redirectedUrl("/time-tracking?success"))
               .andExpect(flash().attribute("alertClass", "alert-success"))
               .andExpect(flash().attributeExists("message"));
    }

    @Test
    @WithMockUser
    void whenUnsuccessfullyClockingOutDisplayWarning() throws Exception {
        long employeeID = 0L;
        ClockResponse clockResponse = ClockResponse.error(employeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN);
        when(timeTrackingUseCase.clockOut(employeeID)).thenReturn(clockResponse);

        mockMvc.perform(post("/time-tracking/clock-out")
                                .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(flash().attributeExists("timeEntries"))
               .andExpect(redirectedUrl("/time-tracking?error"))
               .andExpect(flash().attribute("alertClass", "alert-failure"))
               .andExpect(flash().attributeExists("message"));
    }

    @Test
    @WithMockUser
    void whenPostToClockPauseEmployeeThenClockPauseForEmployee() throws Exception {
        long employeeID = 0L;
        ClockResponse clockResponse = ClockResponse.success(employeeID, TimeEntryType.CLOCK_PAUSE);
        when(timeTrackingUseCase.clockPause(employeeID)).thenReturn(clockResponse);
        mockMvc.perform(post("/time-tracking/clock-pause")
                                .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(flash().attributeExists("timeEntries"))
               .andExpect(redirectedUrl("/time-tracking?success"))
               .andExpect(flash().attribute("alertClass", "alert-success"))
               .andExpect(flash().attributeExists("message"));
    }

    @Test
    @WithMockUser
    void whenUnsuccessfullyClockingPauseDisplayWarning() throws Exception {
        long employeeID = 0L;
        ClockResponse clockResponse = ClockResponse.error(employeeID, ClockError.EMPLOYEE_NOT_CLOCKED_IN);
        when(timeTrackingUseCase.clockPause(employeeID)).thenReturn(clockResponse);

        mockMvc.perform(post("/time-tracking/clock-pause")
                                .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(flash().attributeExists("timeEntries"))
               .andExpect(redirectedUrl("/time-tracking?error"))
               .andExpect(flash().attribute("alertClass", "alert-failure"))
               .andExpect(flash().attributeExists("message"));
    }
}
