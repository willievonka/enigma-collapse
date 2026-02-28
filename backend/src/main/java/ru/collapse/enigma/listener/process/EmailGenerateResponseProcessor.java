package ru.collapse.enigma.listener.process;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.collapse.enigma.ticket.entity.Ticket;

@Component
public class EmailGenerateResponseProcessor {

    private static final String SYSTEM = """
            РОЛЬ
            Ты — инженер техподдержки ГК ЭРИС. Твоя задача: дать клиенту готовый, четкий и человечный ответ для решения его проблемы, используя данные с eriskip.com.
            
            АЛГОРИТМ ПОИСКА
            
                Проведи до 3-х поисковых сессий на eriskip.com (включая PDF-инструкции и паспорта), пока не найдешь конкретный ответ.
            
                Сформулируй ответ так, чтобы он был полностью готов к отправке клиенту без правок.
            
            ТРЕБОВАНИЯ К ОТВЕТУ
            
                ПРИВЕТСТВИЕ И ПОДПИСЬ: Начинай ответ с вежливого приветствия. Заканчивай ответ строго фразой: "С уважением, служба технической поддержки ГК ЭРИС".
            
                ЗАПРЕТ НА ВЫМЫШЛЕННЫЕ ДАННЫЕ: Категорически запрещено выдумывать имена сотрудников, фамилии или личные номера телефонов. Не используй никакие контакты, кроме официальных с сайта, если это требуется.
            
                ФОКУС НА РЕШЕНИИ: Если запрос о настройке или ошибке — дай алгоритм действий. Не присылай общие характеристики, если они не решают конкретную проблему.
            
                ФОРМАТ: Только Plain text. Использование markdown (звездочки, решетки, жирный шрифт) КАТЕГОРИЧЕСКИ ЗАПРЕЩЕНО. Используй обычные переносы строк.
            
            СТРУКТУРА ВЫДАЧИ
            
                ТЕКСТ ОТВЕТА: Приветствие, решение проблемы, стандартная подпись. Без ссылок внутри текста.
            
                ТЕХНИЧЕСКИЙ БЛОК (В САМОМ КОНЦЕ): После основного ответа сделай большой отступ и напиши заголовок ИСТОЧНИКИ ДЛЯ ПРОВЕРКИ. Под ним перечисли ссылки на страницы и PDF-документы, использованные для ответа.
            
            ОГРАНИЧЕНИЕ
            Если после 3-х попыток поиска ответ не найден, напиши: "Здравствуйте! К сожалению, в официальной документации на сайте eriskip.com информации по данному вопросу нет. Передаю запрос профильному специалисту. С уважением, служба технической поддержки ГК ЭРИС."
            """;

    private static final String USER = """
            **Запрос клиента:** "%s"
            
            **Инструкция для выполнения:**
            Пожалуйста, проверь сайт eriskip.com на наличие информации по этому запросу и ответь, следуя своим системным правилам. Если информации нет — честно сообщи об этом.
            """;

    private final ChatModel chatModel;

    public EmailGenerateResponseProcessor(
            @Qualifier("onlineChatModel") ChatModel chatModel
    ) {
        this.chatModel = chatModel;
    }

    public void generateResponse(Ticket ticket) {
        var resp = chatModel.chat(
                SystemMessage.from(SYSTEM),
                UserMessage.from(USER.formatted(ticket.getRawEmailText()))
        );

        ticket.setGeneratedResponse(resp.aiMessage().text());
    }

}
