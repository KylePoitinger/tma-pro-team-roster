package main.java.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProEvent {
    public String eventType;
    public String entityType;
    public Object payload;
    public LocalDateTime timestamp;

    public ProEvent() {
    }

    public ProEvent(String eventType, String entityType, Object payload, LocalDateTime timestamp) {
        this.eventType = eventType;
        this.entityType = entityType;
        this.payload = payload;
        this.timestamp = timestamp;
    }

    public static ProEvent create(String eventType, String entityType, Object payload) {
        return new ProEvent(eventType, entityType, payload, LocalDateTime.now());
    }
}
