package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeManagementService;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WeekReport;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@Import(SecurityConfiguration.class)
@Tag("io")
public class TimeReportMvcTest {

    @MockBean
    TimeTrackingUseCase timeTrackingUseCase;

    @MockBean
    EmployeeManagementService employeeManagementService;

    @MockBean
    AuthenticationUtils authenticationUtils;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReportUseCase reportUseCase;


    @Test
    @WithMockUser
    void whenGetTimeEntriesReturnViewWithTimeEntriesInModel() throws Exception {
        WeekReport weekReport = new WeekReport(1, null, 0, 0);
        when(reportUseCase.getCurrentWeekReportForEmployee(anyLong())).thenReturn(weekReport);

        mockMvc.perform(get("/time-report"))
               .andExpect(status().isOk())
               .andExpect(view().name("timeReport"))
               .andExpect(model().attributeExists("weekReport"));
    }
}
