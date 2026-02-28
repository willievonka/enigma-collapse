package ru.collapse.enigma.ticket.listener.process;

import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.collapse.enigma.ticket.entity.Ticket;

import static ru.collapse.enigma.util.Mapper.toJsonNode;

@RequiredArgsConstructor
@Component
public class EmailCompanyNameProcessor {

    private static final String SYSTEM = """
            Ты — AI-агент технической поддержки.
            
            Твоя задача — проанализировать входящее письмо клиента и определить, от какого юридического лица (компании, предприятия, завода и т.д.) оно отправлено.
            
            Правила:
            
            1. Найди в тексте название компании или юридического лица, от имени которого написано письмо.
            2. Если компания или юридическое лицо указано явно, верни название.
            3. Если такой информации нет в письме, верни NULL.
            4. Возвращай строго JSON в формате:
            
            {
              "company": "<название компании или NULL>"
            }
            
            5. Никакого дополнительного текста.
            6. Никаких объяснений.
            7. Только одно поле — company.
            """;

    private static final String USER = """
            Определи компанию или юридическое лицо, от имени которого написано следующее письмо клиента.
            
            Письмо:
            "
            %s
            "
            """;

    private final ChatModel chatModel;

    public void resolveCompanyName(Ticket ticket) {
        var resp = chatModel.chat(
                SystemMessage.from(SYSTEM),
                UserMessage.from(USER.formatted(ticket.getRawEmailText()))
        );

        JsonNode node = toJsonNode(resp.aiMessage().text());
        String companyName = node.get("company").asText();
        ticket.setCompanyName(companyName);
    }
}
