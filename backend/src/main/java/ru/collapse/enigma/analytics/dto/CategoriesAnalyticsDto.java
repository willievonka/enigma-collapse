package ru.collapse.enigma.analytics.dto;

import java.util.List;

public record CategoriesAnalyticsDto(
        List<String> categories,
        List<Long> values
) {
}

