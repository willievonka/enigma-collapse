package ru.collapse.enigma.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.collapse.enigma.kafka.message.TicketProcessTaskMessage;
import ru.collapse.enigma.listener.process.*;
import ru.collapse.enigma.tg.TelegramService;
import ru.collapse.enigma.ticket.entity.Ticket;
import ru.collapse.enigma.ticket.entity.TicketStatus;
import ru.collapse.enigma.ticket.repository.TicketRepository;

import static ru.collapse.enigma.kafka.KafkaTopic.TICKET_PROCESS_TASK;

@Slf4j
@RequiredArgsConstructor
@Component
public class TicketProcessTaskListener {

    private final TicketRepository ticketRepository;
    private final TelegramService telegramService;

    private final EmailSentinelProcessor emailSentinelProcessor;
    private final EmailCompanyNameProcessor emailCompanyNameProcessor;
    private final EmailDeviceTypeProcessor emailDeviceTypeProcessor;
    private final EmailCategoryProcessor emailCategoryProcessor;
    private final EmailPhoneProcessor emailPhoneProcessor;
    private final EmailGenerateResponseProcessor emailGenerateResponseProcessor;

    @KafkaListener(topics = TICKET_PROCESS_TASK, concurrency = "5")
    public void consume(TicketProcessTaskMessage message) {
        Ticket ticket = ticketRepository.findById(message.id()).orElse(null);
        if (ticket == null) {
            log.error("Received ticket with id={}, but ticket not found", message.id());
            return;
        }
        log.info("Start process email with subject={}", ticket.getSubject());

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
