package br.com.harlemsilvas.itemcontrol.core.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade que representa uma categoria de itens.
 * Permite organização hierárquica (categoria pai → filhos).
 */
public class Category {
    private UUID id;
    private UUID userId;
    private String name;
    private UUID parentId;
    private Instant createdAt;
    private Instant updatedAt;

    private Category(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.userId = builder.userId;
        this.name = builder.name;
        this.parentId = builder.parentId;
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
    }

    // Business methods

    /**
     * Verifica se é uma categoria raiz (sem pai)
     */
    public boolean isRoot() {
        return this.parentId == null;
    }

    /**
     * Atualiza o nome da categoria
     */
    public void updateName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = newName;
        this.updatedAt = Instant.now();
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

    public UUID getParentId() {
        return parentId;
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
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    /**
     * Builder para Category
     */
    public static class Builder {
        private UUID id;
        private UUID userId;
        private String name;
        private UUID parentId;
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

        public Builder parentId(UUID parentId) {
            this.parentId = parentId;
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

        public Category build() {
            return new Category(this);
        }
    }
}
