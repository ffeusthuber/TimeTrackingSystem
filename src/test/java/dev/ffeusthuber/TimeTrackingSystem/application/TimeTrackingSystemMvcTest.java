package dev.ffeusthuber.TimeTrackingSystem.application;

import dev.ffeusthuber.TimeTrackingSystem.adapter.in.EmployeeController;
import dev.ffeusthuber.TimeTrackingSystem.adapter.in.TimeEntriesController;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeManagementService;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.GetTimeEntriesUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.TimeTrackingUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.EmployeeRepository;
import dev.ffeusthuber.TimeTrackingSystem.config.SecurityConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({TimeEntriesController.class, EmployeeController.class})
@Import(SecurityConfiguration.class)
@Tag("io")
public class TimeTrackingSystemMvcTest {

    @MockBean
    TimeTrackingUseCase timeTrackingUseCase;

    @MockBean
    GetTimeEntriesUseCase getTimeEntriesService;

    @MockBean
    EmployeeManagementService employeeManagementService;

    @MockBean
    EmployeeRepository employeeRepository;

    @Autowired
    MockMvc mockMvc;

    @Test
    void whenGetToHomeReturnTimeEntriesView() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("timeEntries"));
    }

    @Test
    void whenGetToTimeEntriesWithOutLoggedInUserRedirectToLogin() throws Exception {
        mockMvc.perform(get("/time-entries"))
               .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    void whenGetTimeEntriesReturnViewWithTimeEntriesInModel() throws Exception {
        mockMvc.perform(get("/time-entries"))
               .andExpect(status().isOk())
               .andExpect(view().name("timeEntries"))
               .andExpect(model().attributeExists("timeEntries"));
    }

    @Test
    @WithMockUser
    void whenPostToClockInEmployeeThenStatusOk() throws Exception {
        mockMvc.perform(post("/time-entries/clock-in")
                                .with(csrf()))
               .andExpect(status().isOk())
               .andExpect(view().name("timeEntries"))
               .andExpect(model().attributeExists("timeEntries"));
    }

    @Test
    @WithMockUser
    void whenPostToClockOutEmployeeThenStatusOk() throws Exception {
        mockMvc.perform(post("/time-entries/clock-out")
                                .with(csrf()))
               .andExpect(status().isOk())
               .andExpect(view().name("timeEntries"))
               .andExpect(model().attributeExists("timeEntries"));
    }

    @Test
    @WithMockUser
    void whenPostToClockPauseEmployeeThenStatusOk() throws Exception {
        mockMvc.perform(post("/time-entries/clock-pause")
                                .with(csrf()))
               .andExpect(status().isOk())
               .andExpect(view().name("timeEntries"))
               .andExpect(model().attributeExists("timeEntries"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenGetToCreateEmployeeAsAdminReturnCreateEmployeeView() throws Exception {
        mockMvc.perform(get("/create-employee"))
               .andExpect(status().isOk())
               .andExpect(view().name("createEmployee"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void whenGetToCreateEmployeeAsUserStatusForbidden() throws Exception {
        mockMvc.perform(get("/create-employee"))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void successfulPostToCreateEmployeeLeadsToSuccess() throws Exception {
        mockMvc.perform(post("/create-employee")
                                .with(csrf())
                                .param("firstName", "Jane")
                                .param("lastName", "Doe")
                                .param("email", "j.doe@test-mail.com")
                                .param("password", "password")
                                .param("role", "USER"))
               .andExpect(redirectedUrl("/create-employee?success"))
               .andExpect(flash().attribute("alertClass", "alert-success"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void postToCreateEmployeeWithDuplicateEmailRedirectsToError() throws Exception {
        String duplicateEmail = "duplicateEmail@test-mail.com";
        doThrow(new DuplicateKeyException("Duplicate email"))
                .when(employeeManagementService)
                .createEmployee("Jane", "Doe", duplicateEmail, "password", "USER");

        mockMvc.perform(post("/create-employee")
                                .with(csrf())
                                .param("firstName", "Jane")
                                .param("lastName", "Doe")
                                .param("email", duplicateEmail)
                                .param("password", "password")
                                .param("role", "USER"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/create-employee?error"))
               .andExpect(flash().attribute("alertClass", "alert-danger"));
    }

}
