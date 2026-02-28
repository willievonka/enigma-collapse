package ru.collapse.enigma.ticket.dto;

import ru.collapse.enigma.ticket.entity.Sentiment;
import ru.collapse.enigma.ticket.entity.TicketStatus;

import java.time.Instant;
import java.util.List;

public record TicketResponseDto(
        Long id,
        String fullName,
        String companyName,
        String phone,
        String email,
        List<String> serialNumbers,
        String deviceType,
        Sentiment sentiment,
        TicketStatus status,
        Instant createdAt
) {
}
