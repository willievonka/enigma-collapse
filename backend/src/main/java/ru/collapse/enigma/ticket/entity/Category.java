package ru.collapse.enigma.ticket.entity;

public enum Category {
    MAINTENANCE("Обслуживание"),
    MODERNIZATION("Модернизация"),
    DIAGNOSTICS("Диагностика"),
    NODE_REPLACEMENT("Замена узла"),
    APPROVAL("Согласование"),
    REPORTING("Отчётность"),
    AUDIT("Аудит"),
    CRITICAL_ERROR("Ошибка"),
    WARNING("Предупреждение"),
    RISK("Риск"),
    SETUP("Настройка"),
    CALIBRATION("Поверка"),
    CONSERVATION("Консервация"),
    TESTING("Тестирование"),
    UNKNOWN("Не определена");

    private final String russian;

    Category(String russian) {
        this.russian = russian;
    }

    public String getRussian() {
        return russian;
    }
}
