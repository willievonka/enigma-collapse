package ru.collapse.enigma.rag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@AllArgsConstructor
@Getter
@ConfigurationProperties(prefix = "qdrant")
public class QdrantProps {

    private String host;
    private int port;
    private String collection;

}
