package ru.collapse.enigma.listener.process;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.collapse.enigma.ticket.entity.Ticket;

import java.util.List;

import static ru.collapse.enigma.listener.Mapper.MAPPER;
import static ru.collapse.enigma.listener.Mapper.toJsonNode;

@RequiredArgsConstructor
@Component
public class EmailDeviceTypeProcessor {

    private static final String SYSTEM = """
            Ты — AI-агент технической поддержки компании Eris, которая производит газовое оборудование.
            
              Твоя задача — определить **тип устройства** и **серийные номера** из письма клиента.
            
              Правила:
            
              - Тип устройства (device) должен быть общим названием устройства, 1–2 слова.
              - Если тип устройства невозможно определить, верни null (не в кавычках).
              - Серийные номера (serialNumbers) — массив строк. Если серийных номеров нет, верни пустой массив [].
              - Ответ строго в JSON формате, без объяснений и лишнего текста.
              - Не используй разметку MARKDOWN, НЕ ОБОРАЧИВАЙ в ```
            
              Пример структуры ответа:
            
              {
                "device": <тип устройства в кавычках или null>,
                "serialNumbers": ["<серийный номер 1>", "<серийный номер 2>", ...]
              }
            
              Примеры:
            
              Письмо: "Наш газоанализатор GA-100 с серийным номером 12345 не включается."
              Ответ:
              {
                "device": "газоанализатор",
                "serialNumbers": ["GA-100", "12345"]
              }
            
              Письмо: "Мы купили котёл GX-200, серийные номера: 56789, 56790. Требуется проверка."
              Ответ:
              {
                "device": "котёл",
                "serialNumbers": ["GX-200", "56789", "56790"]
              }
            
              Письмо: "У нас возник вопрос по вашему оборудованию."
              Ответ:
              {
                "device": null,
                "serialNumbers": []
              }
            """;

    private static final String USER = """
            Определи тип устройства и серийные номера, о которых идёт речь в письме клиента. Верни только JSON.
            
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
        if ("null".equals(deviceType)) {
            deviceType = null;
        }

        List<String> serialNumbers = MAPPER.convertValue(node.get("serialNumbers"), new TypeReference<List<String>>() {
        });
        ticket.setDeviceType(deviceType);
        ticket.setSerialNumbers(serialNumbers);
    }

}
