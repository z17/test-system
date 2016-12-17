package test_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "test_system")
@EnableJpaRepositories("test_system.repository")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}