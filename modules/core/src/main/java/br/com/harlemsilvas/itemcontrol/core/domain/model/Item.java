package br.com.harlemsilvas.itemcontrol.core.domain.model;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.ItemStatus;

import java.time.Instant;
import java.util.*;

/**
 * Agregado raiz - Representa um item controlado pelo sistema.
 * Exemplos: veículo, conta de água, galão de água, etc.
 */
public class Item {
    private UUID id;
    private UUID userId;
    private String name;
    private String nickname;
    private UUID categoryId;
    private String templateCode;
    private ItemStatus status;
    private Set<String> tags;
    private Map<String, Object> metadata;
    private Instant createdAt;
    private Instant updatedAt;

    // Construtor privado para uso do Builder
    private Item(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.userId = builder.userId;
        this.name = builder.name;
        this.nickname = builder.nickname;
        this.categoryId = builder.categoryId;
        this.templateCode = builder.templateCode;
        this.status = builder.status != null ? builder.status : ItemStatus.ACTIVE;
        this.tags = new HashSet<>(builder.tags);
        this.metadata = new HashMap<>(builder.metadata);
        this.createdAt = builder.createdAt != null ? builder.createdAt : Instant.now();
        this.updatedAt = builder.updatedAt != null ? builder.updatedAt : Instant.now();

        validate();
    }

    private void validate() {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (templateCode == null || templateCode.trim().isEmpty()) {
            throw new IllegalArgumentException("TemplateCode cannot be null or empty");
        }
    }

    // Business methods

    /**
     * Atualiza os metadados do item
     */
    public void updateMetadata(Map<String, Object> newMetadata) {
        if (newMetadata != null) {
            this.metadata.putAll(newMetadata);
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Atualiza uma métrica específica nos metadados
     */
    public void updateMetric(String metricName, Object value) {
        if (metricName == null || metricName.trim().isEmpty()) {
            throw new IllegalArgumentException("Metric name cannot be null or empty");
        }
        this.metadata.put(metricName, value);
        this.updatedAt = Instant.now();
    }

    /**
     * Adiciona uma tag ao item
     */
    public void addTag(String tag) {
        if (tag != null && !tag.trim().isEmpty()) {
            this.tags.add(tag.toLowerCase());
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Remove uma tag do item
     */
    public void removeTag(String tag) {
        if (tag != null) {
            this.tags.remove(tag.toLowerCase());
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Arquiva o item
     */
    public void archive() {
        this.status = ItemStatus.ARCHIVED;
        this.updatedAt = Instant.now();
    }

    /**
     * Inativa o item
     */
    public void inactivate() {
        this.status = ItemStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    /**
     * Ativa o item
     */
    public void activate() {
        this.status = ItemStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    /**
     * Verifica se o item está ativo
     */
    public boolean isActive() {
        return this.status == ItemStatus.ACTIVE;
    }

    // Getters

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public Map<String, Object> getMetadata() {
        return Collections.unmodifiableMap(metadata);
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
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", templateCode='" + templateCode + '\'' +
                ", status=" + status +
                ", tags=" + tags +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    /**
     * Builder para Item
     */
    public static class Builder {
        private UUID id;
        private UUID userId;
        private String name;
        private String nickname;
        private UUID categoryId;
        private String templateCode;
        private ItemStatus status;
        private Set<String> tags = new HashSet<>();
        private Map<String, Object> metadata = new HashMap<>();
        private Instant createdAt;
        private Instant updatedAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder categoryId(UUID categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder templateCode(String templateCode) {
            this.templateCode = templateCode;
            return this;
        }

        public Builder status(ItemStatus status) {
            this.status = status;
            return this;
        }

        public Builder tags(Set<String> tags) {
            this.tags = (tags == null) ? new HashSet<>() : new HashSet<>(tags);
            return this;
        }

        public Builder addTag(String tag) {
            this.tags.add(tag);
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = (metadata == null) ? new HashMap<>() : new HashMap<>(metadata);
            return this;
        }

        public Builder addMetadata(String key, Object value) {
            this.metadata.put(key, value);
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

        public Item build() {
            return new Item(this);
        }
    }
}
