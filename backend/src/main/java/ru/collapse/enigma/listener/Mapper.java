package ru.collapse.enigma.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Mapper {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static JsonNode toJsonNode(String message) {
        try {
            return MAPPER.readTree(message);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse json message: {}", message);
            throw new RuntimeException(e);
        }
    }
}
