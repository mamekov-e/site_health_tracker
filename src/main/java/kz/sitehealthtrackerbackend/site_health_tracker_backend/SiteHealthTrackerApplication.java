package kz.sitehealthtrackerbackend.site_health_tracker_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.telegram.telegrambots.starter.TelegramBotStarterConfiguration;

@Import({TelegramBotStarterConfiguration.class})
@SpringBootApplication
public class SiteHealthTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiteHealthTrackerApplication.class, args);
    }
}
