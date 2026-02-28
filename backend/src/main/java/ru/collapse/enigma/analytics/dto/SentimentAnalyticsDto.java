package ru.collapse.enigma.analytics.dto;

import java.util.List;

public record SentimentAnalyticsDto(
        List<String> sentiments,
        List<Long> values
) {
}
