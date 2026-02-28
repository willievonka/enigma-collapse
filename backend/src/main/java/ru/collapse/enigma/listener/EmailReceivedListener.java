package ru.collapse.enigma.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.collapse.enigma.kafka.message.EmailReceivedMessage;
import ru.collapse.enigma.listener.process.*;
import ru.collapse.enigma.mail.send.MailClient;
import ru.collapse.enigma.tg.TelegramService;
import ru.collapse.enigma.ticket.entity.Ticket;
import ru.collapse.enigma.ticket.entity.TicketStatus;
import ru.collapse.enigma.ticket.mapper.TicketMapper;
import ru.collapse.enigma.ticket.repository.TicketRepository;

import static ru.collapse.enigma.kafka.KafkaTopic.EMAIL_RECEIVED;

@RequiredArgsConstructor
@Component
public class EmailReceivedListener {

    private final MailClient mailClient;
    private final TicketMapper ticketMapper;
    private final TicketRepository ticketRepository;
    private final TelegramService telegramService;

    private final EmailSentinelProcessor emailSentinelProcessor;
    private final EmailCompanyNameProcessor emailCompanyNameProcessor;
    private final EmailDeviceTypeProcessor emailDeviceTypeProcessor;
    private final EmailCategoryProcessor emailCategoryProcessor;
    private final EmailPhoneProcessor emailPhoneProcessor;
    private final EmailGenerateResponseProcessor emailGenerateResponseProcessor;

    @KafkaListener(topics = EMAIL_RECEIVED)
    public void consume(EmailReceivedMessage message) {
        Ticket ticket = ticketMapper.toEntity(message);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticketRepository.save(ticket);

        emailSentinelProcessor.calculateSentinel(ticket);
        emailCompanyNameProcessor.resolveCompanyName(ticket);
        emailDeviceTypeProcessor.resolveDeviceType(ticket);
        emailCategoryProcessor.resolveCategory(ticket);
        emailPhoneProcessor.parsePhoneNumber(ticket);
        emailGenerateResponseProcessor.generateResponse(ticket);

        ticket.setStatus(TicketStatus.PROCESSED);
        ticketRepository.save(ticket);

        telegramService.notify(ticket);

//        mailClient.send(message.senderAddress(), "Новое письмо", "Контент");
//        mailClient.reply(message.senderAddress(), message.subject(), "Ответ", message.mailId());
    }

}
