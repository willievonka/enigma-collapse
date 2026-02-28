package ru.collapse.enigma.rag;

import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RagService {

    private final ContentRetriever contentRetriever;

    public List<String> search(String query) {
        return contentRetriever.retrieve(Query.from(query))
                .stream()
                .map(content -> content.textSegment().text())
                .toList();
    }


}
