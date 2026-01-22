package br.com.harlemsilvas.itemcontrol.core.domain.model;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.AlertStatus;
import br.com.harlemsilvas.itemcontrol.core.domain.enums.AlertType;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade que representa um alerta gerado por uma regra.
 */
public class Alert {
    private UUID id;
    private UUID ruleId;
    private UUID itemId;
    private UUID userId;
    private AlertType alertType;
    private String title;
    private String message;
    private Instant triggeredAt;
    private Instant dueAt;
    private AlertStatus status;
    private int priority;
    private Instant createdAt;
    private Instant readAt;
    private Instant completedAt;

    private Alert(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.ruleId = builder.ruleId;
        this.itemId = builder.itemId;
        this.userId = builder.userId;
        this.alertType = builder.alertType;
        this.title = builder.title;
        this.message = builder.message;
        this.triggeredAt = builder.triggeredAt != null ? builder.triggeredAt : Instant.now();
        this.dueAt = builder.dueAt;
        this.status = builder.status != null ? builder.status : AlertStatus.PENDING;
        this.priority = builder.priority;
        this.createdAt = builder.createdAt != null ? builder.createdAt : Instant.now();
        this.readAt = builder.readAt;
        this.completedAt = builder.completedAt;

        validate();
    }

    private void validate() {
        if (ruleId == null) {
            throw new IllegalArgumentException("RuleId cannot be null");
        }
        if (itemId == null) {
            throw new IllegalArgumentException("ItemId cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (alertType == null) {
            throw new IllegalArgumentException("AlertType cannot be null");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("Priority must be between 1 and 5");
        }
    }

    // Business methods

    /**
     * Marca o alerta como lido
     */
    public void markAsRead() {
        if (this.status == AlertStatus.PENDING) {
            this.status = AlertStatus.READ;
            this.readAt = Instant.now();
        }
    }

    /**
     * Dispensa o alerta
     */
    public void dismiss() {
        this.status = AlertStatus.DISMISSED;
    }

    /**
     * Marca o alerta como completo/resolvido
     */
    public void complete() {
        this.status = AlertStatus.COMPLETED;
        this.completedAt = Instant.now();
    }

    /**
     * Verifica se o alerta está pendente
     */
    public boolean isPending() {
        return this.status == AlertStatus.PENDING;
    }

    /**
     * Verifica se o alerta é urgente
     */
    public boolean isUrgent() {
        return this.alertType == AlertType.URGENT;
    }

    /**
     * Verifica se o alerta já passou do prazo (due date)
     */
    public boolean isOverdue() {
        return dueAt != null && Instant.now().isAfter(dueAt);
    }

    // Getters

    public UUID getId() {
        return id;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public UUID getItemId() {
        return itemId;
    }

    public UUID getUserId() {
        return userId;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTriggeredAt() {
        return triggeredAt;
    }

    public Instant getDueAt() {
        return dueAt;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public int getPriority() {
        return priority;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getReadAt() {
        return readAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alert alert = (Alert) o;
        return Objects.equals(id, alert.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Alert{" +
                "id=" + id +
                ", ruleId=" + ruleId +
                ", itemId=" + itemId +
                ", userId=" + userId +
                ", alertType=" + alertType +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", triggeredAt=" + triggeredAt +
                ", dueAt=" + dueAt +
                '}';
    }

    /**
     * Builder para Alert
     */
    public static class Builder {
        private UUID id;
        private UUID ruleId;
        private UUID itemId;
        private UUID userId;
        private AlertType alertType;
        private String title;
        private String message;
        private Instant triggeredAt;
        private Instant dueAt;
        private AlertStatus status;
        private int priority = 3; // Default: prioridade média
        private Instant createdAt;
        private Instant readAt;
        private Instant completedAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder ruleId(UUID ruleId) {
            this.ruleId = ruleId;
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

        public Builder alertType(AlertType alertType) {
            this.alertType = alertType;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder triggeredAt(Instant triggeredAt) {
            this.triggeredAt = triggeredAt;
            return this;
        }

        public Builder dueAt(Instant dueAt) {
            this.dueAt = dueAt;
            return this;
        }

        public Builder status(AlertStatus status) {
            this.status = status;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder readAt(Instant readAt) {
            this.readAt = readAt;
            return this;
        }

        public Builder completedAt(Instant completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public Alert build() {
            return new Alert(this);
        }
    }
}
