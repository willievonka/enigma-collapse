package ru.collapse.enigma.kafka;

public class KafkaSkipException extends RuntimeException {
    public KafkaSkipException(String message) {
        super(message);
    }
}
