package ru.collapse.enigma.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.collapse.enigma.kafka.KafkaTopic;
import ru.collapse.enigma.kafka.message.EmailReceivedMessage;
import ru.collapse.enigma.kafka.message.TicketProcessTaskMessage;
import ru.collapse.enigma.ticket.entity.Ticket;
import ru.collapse.enigma.ticket.entity.TicketStatus;
import ru.collapse.enigma.ticket.mapper.TicketMapper;
import ru.collapse.enigma.ticket.repository.TicketRepository;

import static ru.collapse.enigma.kafka.KafkaTopic.EMAIL_RECEIVED;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailReceivedListener {

    private final TicketMapper ticketMapper;
    private final TicketRepository ticketRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = EMAIL_RECEIVED)
    public void consume(EmailReceivedMessage message) {
        log.info("Saving message with subject={}", message.subject());
        Ticket ticket = ticketMapper.toEntity(message);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticketRepository.save(ticket);

        kafkaTemplate.send(KafkaTopic.TICKET_PROCESS_TASK, String.valueOf(ticket.getId()), new TicketProcessTaskMessage(ticket.getId()));
    }

}
