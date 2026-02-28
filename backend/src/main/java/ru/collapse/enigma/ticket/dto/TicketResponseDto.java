package ru.collapse.enigma.ticket.dto;

import ru.collapse.enigma.ticket.entity.Category;
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
        String subject,
        String rawEmailText,
        List<String> serialNumbers,
        String deviceType,
        Category category,
        Sentiment sentiment,
        TicketStatus status,
        String generatedResponse,
        Instant createdAt
) {
}
