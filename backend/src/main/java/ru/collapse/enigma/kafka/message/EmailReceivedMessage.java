package ru.collapse.enigma.kafka.message;

import lombok.Builder;

import java.util.Date;

@Builder
public record EmailReceivedMessage(
        String senderAddress,
        String senderName,
        Date sentDate,
        String subject,
        String text,
        String mailId
) {
}
