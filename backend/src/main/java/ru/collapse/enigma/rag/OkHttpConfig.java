package ru.collapse.enigma.rag;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class OkHttpConfig {

    @Bean
    public OkHttpClient proxyClient(XrayConfig xrayConfig) {
        return new OkHttpClient.Builder()
                .proxy(new Proxy(
                        Proxy.Type.HTTP,
                        new InetSocketAddress(
                                xrayConfig.getHost(),
                                xrayConfig.getPort()
                        )
                ))
                .build();
    }
}
