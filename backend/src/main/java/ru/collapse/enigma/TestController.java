package ru.collapse.enigma;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Points;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.collapse.enigma.rag.RagService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/")
@RestController
public class TestController {

    private final RagService ragService;
    private final EmbeddingModel embeddingModel;
    private final QdrantClient qdrantClient;
    private final EmbeddingStore<TextSegment> embeddingStore;

    @GetMapping("/test")
    public List<String> test(@RequestParam String name) {
        Embedding queryEmbedding = embeddingModel.embed(name).content();
        log.info("Query embedding dimension: {}", queryEmbedding.dimension());
        log.info("Query embedding vector length: {}", queryEmbedding.vector().length);

        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(1)
                .build();

        List<EmbeddingMatch<TextSegment>> matches = embeddingStore.search(embeddingSearchRequest).matches();
        EmbeddingMatch<TextSegment> embeddingMatch = matches.get(0);


        return ragService.search(name);

    }
}
