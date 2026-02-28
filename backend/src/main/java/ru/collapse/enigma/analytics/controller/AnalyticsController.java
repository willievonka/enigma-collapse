package ru.collapse.enigma.analytics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.collapse.enigma.analytics.dto.CategoriesAnalyticsDto;
import ru.collapse.enigma.analytics.dto.ModelsAnalyticsDto;
import ru.collapse.enigma.analytics.dto.SentimentAnalyticsDto;
import ru.collapse.enigma.analytics.service.AnalyticsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * Получение аналитики по тональности обращений
     * @return DTO с данными по распределению тональностей (позитивные, негативные, нейтральные) и их процентами
     */
    @GetMapping("/sentiment")
    @ResponseStatus(HttpStatus.OK)
    public SentimentAnalyticsDto getSentimentAnalytics() {
        return analyticsService.getSentimentAnalytics();
    }

    /**
     * Топ 10 самых популярных моделей устройств, по которым были созданы обращения
     * @return DTO с данными по моделям и количеством обращений для каждой модели
     */
    @GetMapping("/models")
    @ResponseStatus(HttpStatus.OK)
    public ModelsAnalyticsDto getModelsAnalytics() {
        return analyticsService.getModelsAnalytics();
    }

    /**
     * Получение аналитики по категориям обращений
     * @return DTO с данными по распределению категорий обращений и их количеством
     */
    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public CategoriesAnalyticsDto getCategoriesAnalytics() {
        return analyticsService.getCategoriesAnalytics();
    }
}

