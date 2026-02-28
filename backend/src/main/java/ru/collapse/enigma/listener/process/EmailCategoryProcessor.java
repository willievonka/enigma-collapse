package ru.collapse.enigma.listener.process;

import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.collapse.enigma.ticket.entity.Category;
import ru.collapse.enigma.ticket.entity.Ticket;

import static ru.collapse.enigma.listener.Mapper.toJsonNode;

@RequiredArgsConstructor
@Component
public class EmailCategoryProcessor {

    private static final String SYSTEM = """
            Ты — AI-агент технической поддержки.
            
            Твоя задача — классифицировать входящие письма клиентов в одну из категорий TicketCategory.
            
            Допустимые категории (значения enum):
            
            MAINTENANCE — Обслуживание
            MODERNIZATION — Модернизация
            DIAGNOSTICS — Диагностика
            NODE_REPLACEMENT — Замена узла
            APPROVAL — Согласование
            REPORTING — Отчётность
            AUDIT — Аудит
            CRITICAL_ERROR — Критическая ошибка
            WARNING — Предупреждение
            RISK — Риск
            SETUP — Настройка
            CALIBRATION — Поверка
            CONSERVATION — Консервация
            TESTING — Тестирование
            UNKNOWN — Не удалось определить
            
            Правила:
            
            1. Выбери ровно одну категорию, которая лучше всего описывает письмо клиента.
            2. Если категория не ясна или не указана, верни UNKNOWN.
            3. Ответ строго в JSON формате, **без объяснений и лишнего текста**.
            4. Не используй разметку MARKDOWN, НЕ ОБОРАЧИВАЙ в ```
            
            Пример структуры ответа:
            
            {
              "category": "<значение enum из списка выше>"
            }
            """;

    private static final String USER = """
            Проанализируй письмо клиента и верни JSON с категорией TicketCategory.
            
            Письмо:
            "
            %s
            "
            """;

    private final ChatModel chatModel;

    public void resolveCategory(Ticket ticket) {
        var resp = chatModel.chat(
                SystemMessage.from(SYSTEM),
                UserMessage.from(USER.formatted(ticket.getRawEmailText()))
        );

        JsonNode node = toJsonNode(resp.aiMessage().text());
        Category category = Category.valueOf(node.get("category").asText());
        ticket.setCategory(category);
    }
}
