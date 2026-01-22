package br.com.harlemsilvas.itemcontrol.core.domain.model;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.RuleType;
import br.com.harlemsilvas.itemcontrol.core.domain.valueobject.AlertSettings;
import br.com.harlemsilvas.itemcontrol.core.domain.valueobject.RuleCondition;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade que representa uma regra de alerta para um Item.
 */
public class Rule {
    private UUID id;
    private UUID itemId;
    private UUID userId;
    private RuleType ruleType;
    private String name;
    private RuleCondition condition;
    private AlertSettings alertSettings;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;

    private Rule(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.itemId = builder.itemId;
        this.userId = builder.userId;
        this.ruleType = builder.ruleType;
        this.name = builder.name;
        this.condition = builder.condition;
        this.alertSettings = builder.alertSettings;
        this.enabled = builder.enabled;
        this.createdAt = builder.createdAt != null ? builder.createdAt : Instant.now();
        this.updatedAt = builder.updatedAt != null ? builder.updatedAt : Instant.now();

        validate();
    }

    private void validate() {
        if (itemId == null) {
            throw new IllegalArgumentException("ItemId cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (ruleType == null) {
            throw new IllegalArgumentException("RuleType cannot be null");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        if (alertSettings == null) {
            throw new IllegalArgumentException("AlertSettings cannot be null");
        }
    }

    // Business methods

    /**
     * Habilita a regra
     */
    public void enable() {
        this.enabled = true;
        this.updatedAt = Instant.now();
    }

    /**
     * Desabilita a regra
     */
    public void disable() {
        this.enabled = false;
        this.updatedAt = Instant.now();
    }

    /**
     * Atualiza a condição da regra
     */
    public void updateCondition(RuleCondition newCondition) {
        if (newCondition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        this.condition = newCondition;
        this.updatedAt = Instant.now();
    }

    /**
     * Atualiza as configurações de alerta
     */
    public void updateAlertSettings(AlertSettings newSettings) {
        if (newSettings == null) {
            throw new IllegalArgumentException("AlertSettings cannot be null");
        }
        this.alertSettings = newSettings;
        this.updatedAt = Instant.now();
    }

    /**
     * Verifica se a regra está ativa
     */
    public boolean isEnabled() {
        return this.enabled;
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

    public RuleType getRuleType() {
        return ruleType;
    }

    public String getName() {
        return name;
    }

    public RuleCondition getCondition() {
        return condition;
    }

    public AlertSettings getAlertSettings() {
        return alertSettings;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(id, rule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", userId=" + userId +
                ", ruleType=" + ruleType +
                ", name='" + name + '\'' +
                ", enabled=" + enabled +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    /**
     * Builder para Rule
     */
    public static class Builder {
        private UUID id;
        private UUID itemId;
        private UUID userId;
        private RuleType ruleType;
        private String name;
        private RuleCondition condition;
        private AlertSettings alertSettings;
        private boolean enabled = true; // Default: habilitada
        private Instant createdAt;
        private Instant updatedAt;

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

        public Builder ruleType(RuleType ruleType) {
            this.ruleType = ruleType;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder condition(RuleCondition condition) {
            this.condition = condition;
            return this;
        }

        public Builder alertSettings(AlertSettings alertSettings) {
            this.alertSettings = alertSettings;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Rule build() {
            return new Rule(this);
        }
    }
}
