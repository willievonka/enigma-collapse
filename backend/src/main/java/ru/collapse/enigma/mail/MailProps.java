package ru.collapse.enigma.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@AllArgsConstructor
@Getter
@ConfigurationProperties(prefix = "mail")
public class MailProps {

    private String host;
    private String username;
    private String password;
    private int port;

}
