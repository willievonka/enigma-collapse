package ru.collapse.enigma.rag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@AllArgsConstructor
@Getter
@ConfigurationProperties(prefix = "openrouter")
public class OpenrouterProps {

    private String apiKey;
    private String baseUrl;
    private String modelEmbedding;
    private String modelLlm;

}
