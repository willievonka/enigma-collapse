package ru.collapse.enigma.ticket.mapper;

import org.springframework.stereotype.Component;
import ru.collapse.enigma.kafka.message.EmailReceivedMessage;
import ru.collapse.enigma.ticket.dto.TicketResponseDto;
import ru.collapse.enigma.ticket.entity.Ticket;

@Component
public class TicketMapper {

    public TicketResponseDto toDto(Ticket ticket) {
        return new TicketResponseDto(
                ticket.getId(),
                ticket.getFullName(),
                ticket.getCompanyName(),
                ticket.getPhone(),
                ticket.getEmail(),
                ticket.getSubject(),
                ticket.getRawEmailText(),
                ticket.getSerialNumbers(),
                ticket.getDeviceType(),
                ticket.getCategory(),
                ticket.getSentiment(),
                ticket.getStatus(),
                ticket.getGeneratedResponse(),
                ticket.getCreatedAt()
        );
    }

    public Ticket toEntity(EmailReceivedMessage message) {
        return Ticket.builder()
                .mailId(message.mailId())
                .fullName(message.senderName())
                .email(message.senderAddress())
                .subject(message.subject())
                .rawEmailText(message.text())
                .build();
    }
}
