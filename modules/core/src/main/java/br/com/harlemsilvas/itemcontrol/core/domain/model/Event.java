package br.com.harlemsilvas.itemcontrol.core.domain.model;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.EventType;

import java.time.Instant;
import java.util.*;

/**
 * Entidade que representa um evento/registro histórico de um Item.
 * Exemplos: manutenção realizada, pagamento, leitura de medidor, compra.
 */
public class Event {
    private UUID id;
    private UUID itemId;
    private UUID userId;
    private EventType eventType;
    private Instant eventDate;
    private String description;
    private Map<String, Object> metrics;
    private Instant createdAt;

    private Event(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.itemId = builder.itemId;
        this.userId = builder.userId;
        this.eventType = builder.eventType;
        this.eventDate = builder.eventDate;
        this.description = builder.description;
        this.metrics = new HashMap<>(builder.metrics);
        this.createdAt = builder.createdAt != null ? builder.createdAt : Instant.now();

        validate();
    }

    private void validate() {
        if (itemId == null) {
            throw new IllegalArgumentException("ItemId cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (eventType == null) {
            throw new IllegalArgumentException("EventType cannot be null");
        }
        if (eventDate == null) {
            throw new IllegalArgumentException("EventDate cannot be null");
        }
    }

    /**
     * Obtém o valor de uma métrica específica
     */
    public Object getMetric(String metricName) {
        return metrics.get(metricName);
    }

    /**
     * Verifica se possui uma métrica específica
     */
    public boolean hasMetric(String metricName) {
        return metrics.containsKey(metricName);
    }

    // Getters

    public UUID getId() {
        return id;
    }

    public UUID getItemId() {
        return itemId;
    }

    public UUID getUserId() {
        return userId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Instant getEventDate() {
        return eventDate;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Object> getMetrics() {
        return Collections.unmodifiableMap(metrics);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", userId=" + userId +
                ", eventType=" + eventType +
                ", eventDate=" + eventDate +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * Builder para Event
     */
    public static class Builder {
        private UUID id;
        private UUID itemId;
        private UUID userId;
        private EventType eventType;
        private Instant eventDate;
        private String description;
        private Map<String, Object> metrics = new HashMap<>();
        private Instant createdAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder itemId(UUID itemId) {
            this.itemId = itemId;
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder eventDate(Instant eventDate) {
            this.eventDate = eventDate;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder metrics(Map<String, Object> metrics) {
            this.metrics = new HashMap<>(metrics);
            return this;
        }

        public Builder addMetric(String key, Object value) {
            this.metrics.put(key, value);
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }
}
