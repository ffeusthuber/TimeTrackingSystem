package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TimeReportController.class)
@Import(SecurityConfiguration.class)
@Tag("io")
public class TimeReportMvcTest {

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
    @WithMockUser(roles = "USER")
    void whenGetToTimeReportReturnTimeReportView() throws Exception {
        mockMvc.perform(get("/time-report"))
                .andExpect(status().isOk())
                .andExpect(view().name("timeReport"));
    }

    @Test
    @WithMockUser
    void whenGetTimeEntriesReturnViewWithTimeEntriesInModel() throws Exception {
        mockMvc.perform(get("/time-report"))
               .andExpect(model().attributeExists("workday"));
    }
}
