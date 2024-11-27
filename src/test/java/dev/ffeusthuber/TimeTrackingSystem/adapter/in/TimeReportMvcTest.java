package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeManagementService;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.WeekOfYear;
import dev.ffeusthuber.TimeTrackingSystem.application.dto.WeekReport;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.timeTrackingUseCase.TimeTrackingUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.AuthenticationUtils;
import dev.ffeusthuber.TimeTrackingSystem.config.SecurityConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
    void whenGetWithoutSpecifyingWeekParametersTimeEntriesReturnViewWithCurrentWeekReport() throws Exception {
        int currentYear = LocalDate.now().get(WeekFields.ISO.weekBasedYear());
        int currentWeekNumber = LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear());
        WeekReport weekReport = new WeekReport(new WeekOfYear(currentWeekNumber,currentYear), null, 0, 0);
        when(reportUseCase.getWeekReportForEmployeeAndWeekOfYear(anyLong(),eq(currentWeekNumber),eq(currentYear))).thenReturn(weekReport);

        mockMvc.perform(get("/time-report"))
               .andExpect(status().isOk())
               .andExpect(view().name("timeReport"))
               .andExpect(model().attributeExists("weekReport"));
    }

    @Test
    @WithMockUser
    void whenGetWithValidSpecifyingWeekParametersTimeEntriesReturnViewWithWeekReport() throws Exception {
        int weekNumber = 5;
        int year = 2024;
        WeekReport weekReport = new WeekReport(new WeekOfYear(weekNumber, year), null, 0, 0);
        when(reportUseCase.getWeekReportForEmployeeAndWeekOfYear(anyLong(), eq(weekNumber),eq(year))).thenReturn(weekReport);

        mockMvc.perform(get("/time-report")
                                .param("weekNumber", String.valueOf(weekNumber))
                                .param("year", String.valueOf(year)))
               .andExpect(status().isOk())
               .andExpect(view().name("timeReport"))
               .andExpect(model().attributeExists("weekReport"));
    }
}
