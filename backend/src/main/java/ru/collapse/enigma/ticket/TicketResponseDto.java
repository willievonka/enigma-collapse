package ru.collapse.enigma.ticket;

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
        String sentiment,
        String parsedSummary,
        TicketStatus status,
        Instant createdAt
) {
}
