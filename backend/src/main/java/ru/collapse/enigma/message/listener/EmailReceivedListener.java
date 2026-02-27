package ru.collapse.enigma.message.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.collapse.enigma.kafka.message.EmailReceivedMessage;
import ru.collapse.enigma.mail.send.MailClient;

import static ru.collapse.enigma.kafka.KafkaTopic.EMAIL_RECEIVED;

@RequiredArgsConstructor
@Component
public class EmailReceivedListener {

    private final MailClient mailClient;

    @KafkaListener(topics = EMAIL_RECEIVED)
    public void consume(EmailReceivedMessage message) {
        System.out.println("Received email, info: " + message);

        mailClient.send(message.senderAddress(), "Новое письмо", "Контент");
        mailClient.reply(message.senderAddress(), message.subject(), "Ответ", message.mailId());
    }

}
