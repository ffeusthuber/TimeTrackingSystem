package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.service.EmployeeManagementService;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.changePasswordUseCase.ChangePasswordResponse;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.changePasswordUseCase.ChangePasswordUseCase;
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

import static dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.changePasswordUseCase.ChangePasswordResponseStatus.SUCCESS;
import static dev.ffeusthuber.TimeTrackingSystem.application.port.in.user.changePasswordUseCase.ChangePasswordResponseStatus.WRONG_PASSWORD;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @MockBean
    ChangePasswordUseCase changePasswordUseCase;

    @Test
    @WithMockUser(roles = "USER")
    void whenGetToChangePasswordReturnChangePasswordView() throws Exception {
        mockMvc.perform(get("/change-password"))
               .andExpect(status().isOk())
               .andExpect(view().name("changePassword"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void successfulPostToChangePasswordLeadsToSuccess() throws Exception {
        when(changePasswordUseCase.changePasswordForEmployee(anyLong(), anyString(), anyString())).thenReturn(new ChangePasswordResponse(SUCCESS));
        mockMvc.perform(post("/change-password")
                                .with(csrf())
                                .param("currentPassword", "currentPassword")
                                .param("newPassword", "newPassword")
                                .param("confirmNewPassword", "newPassword"))
               .andExpect(redirectedUrl("/change-password?success"))
               .andExpect(flash().attribute("alertClass", "alert-success"))
               .andExpect(flash().attributeExists("message"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void postToChangePasswordWithNonMatchingNewPasswordsLeadsToError() throws Exception {
        mockMvc.perform(post("/change-password")
                                .with(csrf())
                                .param("currentPassword", "currentPassword")
                                .param("newPassword", "newPassword")
                                .param("confirmNewPassword", "nonMatchingNewPassword"))
               .andExpect(redirectedUrl("/change-password?error"))
               .andExpect(flash().attribute("alertClass", "alert-failure"))
               .andExpect(flash().attributeExists("message"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void postToChangePasswordWithWrongCurrentPasswordLeadsToError() throws Exception {
        when(changePasswordUseCase.changePasswordForEmployee(anyLong(), anyString(), anyString())).thenReturn(new ChangePasswordResponse(WRONG_PASSWORD));
        mockMvc.perform(post("/change-password")
                                .with(csrf())
                                .param("currentPassword", "wrongCurrentPassword")
                                .param("newPassword", "newPassword")
                                .param("confirmNewPassword", "newPassword"))
               .andExpect(redirectedUrl("/change-password?error"))
               .andExpect(flash().attribute("alertClass", "alert-failure"))
               .andExpect(flash().attributeExists("message"));
    }
}
