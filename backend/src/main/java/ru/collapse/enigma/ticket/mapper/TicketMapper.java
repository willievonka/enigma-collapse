package ru.collapse.enigma.ticket.mapper;

import org.springframework.stereotype.Component;
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
                ticket.getSerialNumbers(),
                ticket.getDeviceType(),
                ticket.getSentiment(),
                ticket.getParsedSummary(),
                ticket.getStatus(),
                ticket.getCreatedAt()
        );
    }
}
