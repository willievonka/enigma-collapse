package ru.collapse.enigma.message.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.collapse.enigma.kafka.message.EmailReceivedMessage;

import static ru.collapse.enigma.kafka.KafkaTopic.EMAIL_RECEIVED;

@RequiredArgsConstructor
@Component
public class EmailReceivedListener {

    @KafkaListener(topics = EMAIL_RECEIVED)
    public void consume(EmailReceivedMessage message) {
        System.out.println("Received email, info: " + message);
    }

}
