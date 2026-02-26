package ru.collapse.enigma.mail.read;

import jakarta.mail.*;
import jakarta.mail.search.FlagTerm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.collapse.enigma.mail.MailProps;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailReadHandler {

    private final MailProps mailProps;
    private final MailReaderService mailReaderService;

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void receiveNewMessages() {
        log.info("Start reading email messages");

        try (Store store = connect(mailProps); Folder inbox = store.getFolder("INBOX")) {
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            Arrays.stream(messages).forEach(mailReaderService::processMessage);

        } catch (Exception e) {
            log.error("Failed to read messages", e);
        }
    }

    private Store connect(MailProps mailProps) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        Session session = Session.getInstance(props, null);

        Store store = session.getStore();
        store.connect(mailProps.getHost(), mailProps.getUsername(), mailProps.getPassword());

        return store;
    }

}
