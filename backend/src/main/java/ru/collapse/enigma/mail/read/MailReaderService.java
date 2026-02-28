package ru.collapse.enigma.mail.read;

import jakarta.mail.Flags;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.email.Recipient;
import org.simplejavamail.converter.EmailConverter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.collapse.enigma.kafka.message.EmailReceivedMessage;
import ru.collapse.enigma.kafka.ThrowableSupplier;

import java.util.Optional;

import static ru.collapse.enigma.kafka.KafkaTopic.EMAIL_RECEIVED;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailReaderService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void processMessage(Message message) {
        Email email = mapToEmail(message);
        if (email == null) {
            return;
        }

        log.info("Start reading email message, subject={}", email.getSubject());
        kafkaTemplate.send(EMAIL_RECEIVED, mapToKafkaMessage(email));
    }

    private Email mapToEmail(Message message) {
        if (message instanceof MimeMessage mimeMessage) {
            return EmailConverter.mimeMessageToEmail(mimeMessage);
        }

        log.warn("Can not cast message to MimeMessage, subject={}. Skip...", safeGet(message::getContent));
        markAsRead(message);
        return null;
    }

    private EmailReceivedMessage mapToKafkaMessage(Email email) {
        return EmailReceivedMessage.builder()
                .senderAddress(Optional.ofNullable(email.getFromRecipient()).map(Recipient::getAddress).orElse(null))
                .senderName(Optional.ofNullable(email.getFromRecipient()).map(Recipient::getName).orElse(null))
                .sentDate(email.getSentDate())
                .subject(email.getSubject())
                .text(toPlainText(email))
                .mailId(email.getId())
                .build();
    }

    private <T> Optional<T> safeGet(ThrowableSupplier<T> getAction) {
        try {
            return Optional.ofNullable(getAction.get());
        } catch (Exception e) {
            log.error("Failed to get field from email message", e);
        }

        return Optional.empty();
    }

    private void markAsRead(Message message) {
        try {
            message.setFlag(Flags.Flag.SEEN, true);
        } catch (MessagingException e) {
            log.error("Failed to mark as read", e);
        }
    }

    private String toPlainText(Email email) {
        if (email.getPlainText() != null) {
            return email.getPlainText();
        }

        String htmlTextSafe = Optional.ofNullable(email.getHTMLText()).orElse("");
        return Jsoup.parse(htmlTextSafe).text();
    }
}
