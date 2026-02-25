package ru.collapse.enigma.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.collapse.enigma.mail.MailProps;

@EnableScheduling
@EnableConfigurationProperties(MailProps.class)
@Configuration
public class AppConfig {
}
