package ru.collapse.enigma.analytics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.collapse.enigma.analytics.dto.CategoriesAnalyticsDto;
import ru.collapse.enigma.analytics.dto.ModelsAnalyticsDto;
import ru.collapse.enigma.analytics.dto.SentimentAnalyticsDto;
import ru.collapse.enigma.ticket.repository.TicketRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TicketRepository ticketRepository;

    public SentimentAnalyticsDto getSentimentAnalytics() {
        List<Object[]> rows = ticketRepository.countBySentiment();

        List<String> sentiments = new ArrayList<>();
        List<Long> values = new ArrayList<>();

        for (Object[] row : rows) {
            sentiments.add((String) row[0]);
            values.add(((Number) row[1]).longValue());
        }

        return new SentimentAnalyticsDto(sentiments, values);
    }

    public ModelsAnalyticsDto getModelsAnalytics() {
        List<Object[]> rows = ticketRepository.findTopSerialNumbers();

        List<String> models = new ArrayList<>();
        List<Long> values = new ArrayList<>();

        for (Object[] row : rows) {
            models.add((String) row[0]);
            values.add(((Number) row[1]).longValue());
        }

        return new ModelsAnalyticsDto(models, values);
    }

    public CategoriesAnalyticsDto getCategoriesAnalytics() {
        List<Object[]> rows = ticketRepository.countByCategory();

        List<String> categories = new ArrayList<>();
        List<Long> values = new ArrayList<>();

        for (Object[] row : rows) {
            categories.add((String) row[0]);
            values.add(((Number) row[1]).longValue());
        }

        return new CategoriesAnalyticsDto(categories, values);
    }
}

