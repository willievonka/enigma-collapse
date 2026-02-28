package ru.collapse.enigma;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import io.qdrant.client.QdrantClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.collapse.enigma.rag.RagService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/")
@RestController
public class TestControllerSecond {

    private final RagService ragService;
    private final EmbeddingModel embeddingModel;
    private final QdrantClient qdrantClient;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final ChatModel chatModel;

    @GetMapping("/test")
    public String test(@RequestParam String name) {
        var resp = chatModel.chat(
                SystemMessage.from("Ты опытный backend разработчик"),
                UserMessage.from("Что лучше: Kafka или RabbitMQ?")
        );

        return resp.aiMessage().text();
    }
}
