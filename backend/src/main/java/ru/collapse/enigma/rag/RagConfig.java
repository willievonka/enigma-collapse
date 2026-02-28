package ru.collapse.enigma.rag;


import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.http.client.jdk.JdkHttpClient;
import dev.langchain4j.http.client.jdk.JdkHttpClientBuilder;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;

@Configuration
public class RagConfig {

    @Bean
    public QdrantClient qdrantClient(QdrantProps qdrantProps) {
        return new QdrantClient(
                QdrantGrpcClient
                        .newBuilder(qdrantProps.getHost(), qdrantProps.getPort(), false, true)
                        .build()
        );

    }
//
//    @Bean
//    public OpenAiClient openAiClient(OpenrouterProps openrouterProps, XrayConfig xrayConfig) {
//
//
//        return OpenAiClient.builder()
//                .apiKey(openrouterProps.getApiKey())
//                .baseUrl(openrouterProps.getBaseUrl())
//                .httpClientBuilder(jdkHttpClientBuilder)
//                .build();
//    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(QdrantClient qdrantClient, QdrantProps qdrantProps) {
        return QdrantEmbeddingStore.builder()
                .client(qdrantClient)
                .collectionName(qdrantProps.getCollection())
                .payloadTextKey("page_content")
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel(OpenrouterProps openrouterProps, XrayConfig xrayConfig) {
        HttpClient.Builder httpClientBuilder = HttpClient.newBuilder()
                .proxy(ProxySelector.of(new InetSocketAddress(xrayConfig.getHost(), xrayConfig.getPort())));

        JdkHttpClientBuilder jdkHttpClientBuilder = JdkHttpClient.builder()
                .httpClientBuilder(httpClientBuilder);

        return OpenAiEmbeddingModel.builder()
                .baseUrl(openrouterProps.getBaseUrl())
                .apiKey(openrouterProps.getApiKey())
                .modelName(openrouterProps.getModelEmbedding())
                .dimensions(1536)
                .httpClientBuilder(jdkHttpClientBuilder)
                .build();
    }

    @Bean
    public ContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(5)
                .build();
    }

}
