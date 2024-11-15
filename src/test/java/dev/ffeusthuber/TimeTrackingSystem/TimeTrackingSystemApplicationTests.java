package dev.ffeusthuber.TimeTrackingSystem;

import dev.ffeusthuber.TimeTrackingSystem.adapter.in.InitialAdminCreator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@Tag("io")
class TimeTrackingSystemApplicationTests {

	@MockBean
	InitialAdminCreator initialAdminCreator; //needed for application startUp

	@Test
	void contextLoads() {
	}

}
