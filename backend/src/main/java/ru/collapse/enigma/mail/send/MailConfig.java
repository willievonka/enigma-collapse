package ru.collapse.enigma.mail.send;

import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.collapse.enigma.mail.MailProps;

@Configuration
public class MailConfig {

    @Bean
    public Mailer mailer(MailProps mailProps) {
        return MailerBuilder
                .withSMTPServer(mailProps.getSmtpHost(), mailProps.getSmtpPort(), mailProps.getUsername(), mailProps.getPassword())
                .withProperty("mail.smtp.ssl.enable", true)
                .buildMailer();
    }

}
