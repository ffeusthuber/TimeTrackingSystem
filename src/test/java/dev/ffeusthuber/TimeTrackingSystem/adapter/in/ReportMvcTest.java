package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.dto.WeekReport;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.ReportUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.timeTrackingUseCase.TimeTrackingUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.AuthenticationUtils;
import dev.ffeusthuber.TimeTrackingSystem.application.service.EmployeeManagementService;
import dev.ffeusthuber.TimeTrackingSystem.application.service.WeekOfYear;
import dev.ffeusthuber.TimeTrackingSystem.config.SecurityConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@Import(SecurityConfiguration.class)
@Tag("io")
public class ReportMvcTest {

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

    private int currentYear;
    private int currentWeekNumber;
    private long mockEmployeeId;

    @BeforeEach
    void setup() {
        this.currentYear = LocalDate.now().get(WeekFields.ISO.weekBasedYear());
        this.currentWeekNumber = LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear());
        this.mockEmployeeId = 123L;

        when(authenticationUtils.getAuthenticatedEmployeeID()).thenReturn(mockEmployeeId);
    }


    @Test
    @WithMockUser
    void whenGetWithoutSpecifyingWeekParametersReturnsViewWithCurrentWeekReport() throws Exception {
        WeekReport expectedWeekReport = new WeekReport(new WeekOfYear(currentWeekNumber,currentYear), null, 0, 0, null);
        when(reportUseCase.getWeekReportForEmployeeAndWeekOfYear(eq(mockEmployeeId),eq(currentWeekNumber),eq(currentYear)))
                .thenReturn(expectedWeekReport);

        mockMvc.perform(get("/time-report"))
               .andExpect(status().isOk())
               .andExpect(view().name("timeReport"))
               .andExpect(model().attribute("weekReport", expectedWeekReport));
    }

    @Test
    @WithMockUser
    void whenGetWithValidSpecifyingWeekParametersReturnsViewWithWeekReport() throws Exception {
        int weekNumber = 5;
        int year = 2024;
        WeekReport expectedWeekReport = new WeekReport(new WeekOfYear(weekNumber, year), null, 0, 0, null);
        when(reportUseCase.getWeekReportForEmployeeAndWeekOfYear(eq(mockEmployeeId), eq(weekNumber),eq(year)))
                .thenReturn(expectedWeekReport);

        mockMvc.perform(get("/time-report")
                                .param("weekNumber", String.valueOf(weekNumber))
                                .param("year", String.valueOf(year)))
               .andExpect(status().isOk())
               .andExpect(view().name("timeReport"))
               .andExpect(model().attribute("weekReport", expectedWeekReport));
    }

    @Test
    @WithMockUser
    void whenGetWithInvalidSpecifyingWeekParametersReturnsViewWithCurrentWeekReportAndErrorMessage() throws Exception {
        int invalidWeekNumber = 0;
        int year = 2021;
        String errorMessage = "Week number must be between 1 and 52 for year 2021";
        WeekReport expectedWeekReport = new WeekReport(new WeekOfYear(currentWeekNumber, currentYear), null, 0, 0, errorMessage);
        when(reportUseCase.getWeekReportForEmployeeAndWeekOfYear(eq(mockEmployeeId), eq(invalidWeekNumber),eq(year))).thenReturn(expectedWeekReport);

        MvcResult result = mockMvc.perform(get("/time-report")
                                .param("weekNumber", String.valueOf(invalidWeekNumber))
                                .param("year", String.valueOf(year)))
               .andExpect(status().isOk()).andReturn();

        WeekReport weekReport = (WeekReport) result.getModelAndView().getModel().get("weekReport");
        assertThat(weekReport).isNotNull();
        assertThat(weekReport.errorMessage()).isEqualTo("Week number must be between 1 and 52 for year 2021");
    }

    @Test
    @WithMockUser
    void getWithValidEmployeeIDAndWithoutWithoutSpecifyingWeekParametersReturnsViewWithEmployeesCurrentWeekReport() throws Exception {
        WeekReport expectedWeekReport = new WeekReport(new WeekOfYear(currentWeekNumber,currentYear), null, 0, 0, null);
        when(reportUseCase.getWeekReportForEmployeeAndWeekOfYear(eq(mockEmployeeId),eq(currentWeekNumber),eq(currentYear)))
                .thenReturn(expectedWeekReport);

        mockMvc.perform(get("/employees/time-report")
                                .param("employeeID", "123"))
               .andExpect(status().isOk())
               .andExpect(view().name("timeReport"))
               .andExpect(model().attribute("weekReport", expectedWeekReport));
    }
}
