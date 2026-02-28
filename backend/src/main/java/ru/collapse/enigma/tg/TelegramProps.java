package ru.collapse.enigma.tg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "telegram")
public class TelegramProps {

    private String botToken;
    private String chatId;

}
