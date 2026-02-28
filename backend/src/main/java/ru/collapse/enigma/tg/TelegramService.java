package ru.collapse.enigma.tg;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.collapse.enigma.ticket.entity.Ticket;

@RequiredArgsConstructor
@Component
public class TelegramService {

    private static final String TEMPLATE = """
            *ОТ:* %s
            *ТЕМА:* %s,
            *ТОНАЛЬНОСТЬ:* %s,
            *КАТЕГОРИЯ:* %s
            """;

    private final TelegramProps telegramProps;
    private final TelegramClient telegramClient;

    public void notify(Ticket ticket) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(telegramProps.getChatId())
                .text(buildText(ticket))
                .parseMode(ParseMode.MARKDOWNV2)
                .build();

        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    private String buildText(Ticket ticket) {
        return TEMPLATE.formatted(
                ticket.getEmail(),
                ticket.getSubject(),
                ticket.getSentiment().name(),
                ticket.getCategory().name()
        );
    }

}
