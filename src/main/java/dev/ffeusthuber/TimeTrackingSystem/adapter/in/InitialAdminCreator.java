package dev.ffeusthuber.TimeTrackingSystem.adapter.in;

import dev.ffeusthuber.TimeTrackingSystem.application.port.in.setup.CreateInitialAdminUseCase;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:properties/initial-admin.properties")
@Component
public class InitialAdminCreator {

    @Value("${admin.firstname}")
    private String adminFirstName;

    @Value("${admin.lastname}")
    private String adminLastName;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private final CreateInitialAdminUseCase createInitialAdminUseCase;

    public InitialAdminCreator(CreateInitialAdminUseCase createInitialAdminUseCase) {
        this.createInitialAdminUseCase = createInitialAdminUseCase;
    }

    @PostConstruct
    public void createInitialAdminOnApplicationStartup() {
        createInitialAdminUseCase.createInitialAdmin(
                adminFirstName,
                adminLastName,
                adminEmail,
                adminPassword);
    }
}
