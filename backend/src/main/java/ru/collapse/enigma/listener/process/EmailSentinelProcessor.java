package ru.collapse.enigma.listener.process;

import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.collapse.enigma.ticket.entity.Sentiment;
import ru.collapse.enigma.ticket.entity.Ticket;

import static ru.collapse.enigma.listener.Mapper.toJsonNode;

@RequiredArgsConstructor
@Component
public class EmailSentinelProcessor {

    private static final String SYSTEM = """
            Ты — AI-агент технической поддержки.
            
            Твоя задача — анализировать входящие письма клиентов и определять их эмоциональную тональность.
            
            Ты должен классифицировать письмо ТОЛЬКО в одну из трёх категорий:
            
            - POSITIVE
            - NEUTRAL
            - NEGATIVE
            
            Определения:
            
            POSITIVE — клиент выражает благодарность, удовлетворённость, вежливость или позитивный опыт.
            
            NEUTRAL — клиент спокойно описывает ситуацию, задаёт вопрос или сообщает о проблеме без выраженной эмоции.
            
            NEGATIVE — клиент выражает недовольство, раздражение, жалобу, срочность, разочарование или давление.
            
            Правила ответа:
            
            Ты ОБЯЗАН вернуть результат строго в формате JSON.
            
            Нельзя добавлять объяснения.
            Нельзя добавлять текст вне JSON.
            Нельзя добавлять дополнительные поля.
            Не используй разметку MARKDOWN, НЕ ОБОРАЧИВАЙ в ```
            
            Верни ТОЛЬКО:
            
            {
              "sentiment": "<POSITIVE | NEUTRAL | NEGATIVE>"
            }
            """;

    private static final String USER = """
            Определи тональность следующего письма клиента.
            
            Письмо:
            "
            %s
            "
            """;

    private final ChatModel chatModel;

    public void calculateSentinel(Ticket ticket) {
        var resp = chatModel.chat(
                SystemMessage.from(SYSTEM),
                UserMessage.from(USER.formatted(ticket.getRawEmailText()))
        );

        JsonNode node = toJsonNode(resp.aiMessage().text());
        Sentiment sentiment = Sentiment.valueOf(node.get("sentiment").asText());
        ticket.setSentiment(sentiment);
    }

}
