package ru.collapse.enigma.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.collapse.enigma.mail.MailProps;
import ru.collapse.enigma.rag.OpenrouterProps;
import ru.collapse.enigma.rag.QdrantProps;
import ru.collapse.enigma.rag.XrayConfig;
import ru.collapse.enigma.tg.TelegramProps;

@EnableScheduling
@EnableConfigurationProperties({MailProps.class, OpenrouterProps.class,
        QdrantProps.class, XrayConfig.class, TelegramProps.class})
@Configuration
public class AppConfig {
}
