package ru.collapse.enigma.ticket.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Sentiment {
    POSITIVE("Позитив"),
    NEGATIVE("Нейтраль"),
    NEUTRAL("Негатив");

    private final String russian;
}
