package main.java.dto;

import java.time.LocalDateTime;

public record ProEvent(
    String eventType,
    String entityType,
    Object payload,
    LocalDateTime timestamp
) {
    public static ProEvent create(String eventType, String entityType, Object payload) {
        return new ProEvent(eventType, entityType, payload, LocalDateTime.now());
    }
}
