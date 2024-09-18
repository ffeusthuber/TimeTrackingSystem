package dev.ffeusthuber.TimeTrackingSystem.application;

import dev.ffeusthuber.TimeTrackingSystem.application.port.in.GetTimeEntriesUseCase;
import dev.ffeusthuber.TimeTrackingSystem.application.port.in.TimeTrackingUseCase;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(TimeTrackingSystemController.class)
@Tag("io")
public class TimeTrackingSystemMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TimeTrackingUseCase timeTrackingUseCase;

    @MockBean
    GetTimeEntriesUseCase getTimeEntriesService;

    @Test
    void homePageForwardsToIndexHtml() throws Exception {

        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("home"));
    }
}
