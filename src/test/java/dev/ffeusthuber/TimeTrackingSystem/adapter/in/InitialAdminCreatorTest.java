package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.port.in.setup.CreateInitialAdminUseCase;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "admin.firstname=testFirstName",
        "admin.lastname=testLastName",
        "admin.email=testEmail@test.com",
        "admin.password=testPassword"
})
@Tag("io")
class InitialAdminCreatorTest {

    @MockBean
    private CreateInitialAdminUseCase createInitialAdminUseCase;

    @Autowired
    private InitialAdminCreator initialAdminCreator;

    @Test
    void createInitialAdminIsCalledIfApplicationIsStarted() {
        Mockito.verify(createInitialAdminUseCase, Mockito.times(1))
               .createInitialAdmin("testFirstName", "testLastName", "testEmail@test.com", "testPassword");
    }
}