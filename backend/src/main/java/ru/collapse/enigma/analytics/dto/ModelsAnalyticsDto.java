package ru.collapse.enigma.analytics.dto;

import java.util.List;

public record ModelsAnalyticsDto(
        List<String> models,
        List<Long> values
) {
}

