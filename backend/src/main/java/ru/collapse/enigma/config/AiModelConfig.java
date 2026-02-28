package ru.collapse.enigma.config;

import dev.langchain4j.http.client.jdk.JdkHttpClient;
import dev.langchain4j.http.client.jdk.JdkHttpClientBuilder;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.collapse.enigma.rag.OpenrouterProps;
import ru.collapse.enigma.rag.XrayConfig;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;

@Configuration
public class AiModelConfig {

    @Primary
    @Bean
    public ChatModel chatModel(OpenrouterProps openrouterProps, XrayConfig xrayConfig) {
        HttpClient.Builder httpClientBuilder = HttpClient.newBuilder()
                .proxy(ProxySelector.of(new InetSocketAddress(xrayConfig.getHost(), xrayConfig.getPort())));

        JdkHttpClientBuilder jdkHttpClientBuilder = JdkHttpClient.builder()
                .httpClientBuilder(httpClientBuilder);

        return OpenAiChatModel.builder()
                .baseUrl(openrouterProps.getBaseUrl())
                .apiKey(openrouterProps.getApiKey())
                .modelName(openrouterProps.getModelLlm())
                .httpClientBuilder(jdkHttpClientBuilder)
                .build();
    }

    @Qualifier("onlineChatModel")
    @Bean
    public ChatModel onlineChatModel(OpenrouterProps openrouterProps, XrayConfig xrayConfig) {
        HttpClient.Builder httpClientBuilder = HttpClient.newBuilder()
                .proxy(ProxySelector.of(new InetSocketAddress(xrayConfig.getHost(), xrayConfig.getPort())));

        JdkHttpClientBuilder jdkHttpClientBuilder = JdkHttpClient.builder()
                .httpClientBuilder(httpClientBuilder);

        return OpenAiChatModel.builder()
                .baseUrl(openrouterProps.getBaseUrl())
                .apiKey(openrouterProps.getApiKey())
                .modelName(openrouterProps.getOnlineModelLlm())
                .httpClientBuilder(jdkHttpClientBuilder)
                .build();
    }

}
