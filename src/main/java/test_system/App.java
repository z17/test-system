package test_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(scanBasePackages = "test_system")
@EnableJpaRepositories("test_system.repository")
@Configuration
@EnableWebSecurity
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}