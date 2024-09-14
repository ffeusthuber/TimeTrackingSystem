package dev.ffeusthuber.TimeTrackingSystem.application;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TimeTrackingSystemController.class)
@Tag("io")
public class TimeTrackingSystemMvcTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void homePageForwardsToIndexHtml() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(forwardedUrl("index.html"));
    }
}
