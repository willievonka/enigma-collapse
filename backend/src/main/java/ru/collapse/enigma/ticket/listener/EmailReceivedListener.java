package ru.collapse.enigma.ticket.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.collapse.enigma.kafka.message.EmailReceivedMessage;
import ru.collapse.enigma.mail.send.MailClient;
import ru.collapse.enigma.ticket.entity.Ticket;
import ru.collapse.enigma.ticket.listener.process.EmailCompanyNameProcessor;
import ru.collapse.enigma.ticket.listener.process.EmailDeviceTypeProcessor;
import ru.collapse.enigma.ticket.listener.process.EmailSentinelProcessor;
import ru.collapse.enigma.ticket.mapper.TicketMapper;

import static ru.collapse.enigma.kafka.KafkaTopic.EMAIL_RECEIVED;

@RequiredArgsConstructor
@Component
public class EmailReceivedListener {

    private final MailClient mailClient;
    private final TicketMapper ticketMapper;

    private final EmailSentinelProcessor emailSentinelProcessor;
    private final EmailCompanyNameProcessor emailCompanyNameProcessor;
    private final EmailDeviceTypeProcessor emailDeviceTypeProcessor;

    @KafkaListener(topics = EMAIL_RECEIVED)
    public void consume(EmailReceivedMessage message) {
        Ticket ticket = ticketMapper.toEntity(message);
        emailSentinelProcessor.calculateSentinel(ticket);
        emailCompanyNameProcessor.resolveCompanyName(ticket);
        emailDeviceTypeProcessor.resolveDeviceType(ticket);


        System.out.println();
//        mailClient.send(message.senderAddress(), "Новое письмо", "Контент");
//        mailClient.reply(message.senderAddress(), message.subject(), "Ответ", message.mailId());
    }

}
