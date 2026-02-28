package ru.collapse.enigma.tg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramConfig {

    @Bean
    public TelegramClient telegramClient(TelegramProps telegramProps) {
        return new OkHttpTelegramClient(telegramProps.getBotToken());
    }

}
