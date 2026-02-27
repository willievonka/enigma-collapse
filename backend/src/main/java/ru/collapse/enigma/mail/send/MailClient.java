package ru.collapse.enigma.mail.send;

import lombok.RequiredArgsConstructor;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.stereotype.Component;
import ru.collapse.enigma.mail.MailProps;

@RequiredArgsConstructor
@Component
public class MailClient {

    private static final String FROM = "Служба поддержки";

    private final Mailer mailer;
    private final MailProps mailProps;

    public void send(String to, String subject, String text) {
        Email email = EmailBuilder.startingBlank()
                .from(FROM, mailProps.getUsername())
                .to(to)
                .withSubject(subject)
                .withPlainText(text)
                .buildEmail();
        mailer.sendMail(email);
    }

    public void reply(String to, String originalSubject, String text, String mailId) {
        Email email = EmailBuilder.startingBlank()
                .from(FROM, mailProps.getUsername())
                .to(to)
                .withSubject("RE: " + originalSubject)
                .withPlainText(text)
                .withHeader("In-Reply-To", mailId)
                .withHeader("References", mailId)
                .buildEmail();
        mailer.sendMail(email);
    }

}
