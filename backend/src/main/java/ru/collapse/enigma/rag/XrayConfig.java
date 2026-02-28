package ru.collapse.enigma.rag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "xray")
public class XrayConfig {

    private String host;
    private int port;

}
