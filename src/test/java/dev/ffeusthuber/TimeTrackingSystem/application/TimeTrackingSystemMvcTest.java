package dev.ffeusthuber.TimeTrackingSystem.application;

import dev.ffeusthuber.TimeTrackingSystem.adapter.in.EmployeeController;
import dev.ffeusthuber.TimeTrackingSystem.adapter.in.TimeTrackingSystemController;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeService;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.GetTimeEntriesUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.TimeTrackingUseCase;
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

@WebMvcTest({TimeTrackingSystemController.class, EmployeeController.class})
@Import(SecurityConfiguration.class)
@Tag("io")
public class TimeTrackingSystemMvcTest {

    @MockBean
    TimeTrackingUseCase timeTrackingUseCase;

    @MockBean
    GetTimeEntriesUseCase getTimeEntriesService;

    @MockBean
    EmployeeService employeeService;


    @Autowired
    MockMvc mockMvc;


    @Test
    void whenGetToHomeReturnHomeView() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("home"));

        mockMvc.perform(get("/home"))
               .andExpect(status().isOk())
               .andExpect(view().name("home"));
    }

    @Test
    void whenGetToTimeEntriesWithOutLoggedInUserRedirectToLogin() throws Exception {
        mockMvc.perform(get("/time-entries"))
               .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    void whenGetTimeEntriesWithEmployeeIDInModelReturnTimeEntriesView() throws Exception {
        mockMvc.perform(get("/time-entries")
                                .flashAttr("employeeID", 1L))
               .andExpect(status().isOk())
               .andExpect(view().name("timeEntries"));
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
                .when(employeeService)
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
