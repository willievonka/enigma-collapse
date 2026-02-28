package ru.collapse.enigma.listener.process;

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
public class EmailDeviceTypeProcessor {

    private static final String SYSTEM = """
            Ты — AI-агент технической поддержки компании Eris, которая производит газовое оборудование.
            
            Твоя задача — определить, **о каком устройстве идёт речь** в письме клиента.
            
            Правила:
            
            - Тип устройства (device) должен быть 1–2 словами, описывающими конкретный тип устройства.
            - Если устройство невозможно определить точно или оно явно не указано, верни "unknown".
            - Ответ строго в JSON формате, **без объяснений и дополнительного текста**.
            
            Пример структуры ответа:
            
            {
              "device": "<название устройства или null>"
            }
            """;

    private static final String USER = """
            Определи тип устройства, о котором идёт речь в письме клиента. Верни только JSON.
            
            Письмо:
            "
            %s
            "
            """;

    private final ChatModel chatModel;

    public void resolveDeviceType(Ticket ticket) {
        var resp = chatModel.chat(
                SystemMessage.from(SYSTEM),
                UserMessage.from(USER.formatted(ticket.getRawEmailText()))
        );

        JsonNode node = toJsonNode(resp.aiMessage().text());
        String deviceType = node.get("device").asText();
        ticket.setDeviceType(deviceType);
    }

}
