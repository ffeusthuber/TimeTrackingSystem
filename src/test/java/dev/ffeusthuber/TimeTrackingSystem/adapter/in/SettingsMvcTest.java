package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeManagementService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = SettingsController.class)
@Import(SecurityConfiguration.class)
@Tag("io")
public class SettingsMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeManagementService employeeManagementService;

    @MockBean
    AuthenticationUtils authenticationUtils;

    @Test
    @WithMockUser(roles = "USER")
    void whenGetToChangePasswordReturnChangePasswordView() throws Exception {
        mockMvc.perform(get("/change-password"))
               .andExpect(status().isOk())
               .andExpect(view().name("changePassword"));
    }
}
