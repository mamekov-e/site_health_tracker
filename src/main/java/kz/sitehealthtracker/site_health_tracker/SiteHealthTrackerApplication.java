package kz.sitehealthtracker.site_health_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.starter.TelegramBotStarterConfiguration;

@Import({TelegramBotStarterConfiguration.class})
@EnableScheduling
@SpringBootApplication
public class SiteHealthTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiteHealthTrackerApplication.class, args);
    }

}
