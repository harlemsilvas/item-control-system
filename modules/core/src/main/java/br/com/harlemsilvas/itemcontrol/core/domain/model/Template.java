package br.com.harlemsilvas.itemcontrol.core.domain.model;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Representa um Template de Item.
 *
 * Regras principais:
 * - Pode ser GLOBAL (disponível a todos) ou USER (privado de um usuário)
 * - Suporta múltiplos idiomas via mapas (locale -> texto)
 */
public class Template {

    private final UUID id;

    /**
     * Dono do template quando scope=USER.
     * Quando scope=GLOBAL, este campo pode ser null.
     */
    private final UUID userId;

    private final TemplateScope scope;

    /**
     * Código estável para referenciar o template (ex: VEHICLE).
     */
    private final String code;

    /**
     * Nome traduzido (locale -> texto). Ex: {"pt-BR": "Veículo", "en-US": "Vehicle"}
     */
    private final Map<String, String> nameByLocale;

    /**
     * Descrição traduzida (locale -> texto).
     */
    private final Map<String, String> descriptionByLocale;

    /**
     * Schema/definição da metadata (ex: campos, tipos, required etc.).
     * Mantemos como Map flexível por enquanto para acelerar evolução.
     */
    private final Map<String, Object> metadataSchema;

    private final Instant createdAt;
    private final Instant updatedAt;

    private Template(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.userId = builder.userId;
        this.scope = builder.scope;
        this.code = builder.code;
        this.nameByLocale = new HashMap<>(builder.nameByLocale);
        this.descriptionByLocale = new HashMap<>(builder.descriptionByLocale);
        this.metadataSchema = new HashMap<>(builder.metadataSchema);
        this.createdAt = builder.createdAt != null ? builder.createdAt : Instant.now();
        this.updatedAt = builder.updatedAt != null ? builder.updatedAt : Instant.now();
        validate();
    }

    private void validate() {
        if (scope == null) {
            throw new IllegalArgumentException("Template scope cannot be null");
        }
        if (scope == TemplateScope.USER && userId == null) {
            throw new IllegalArgumentException("UserId cannot be null when scope is USER");
        }
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Template code cannot be null or empty");
        }
        if (nameByLocale.isEmpty()) {
            throw new IllegalArgumentException("Template nameByLocale cannot be empty");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public TemplateScope getScope() {
        return scope;
    }

    public String getCode() {
        return code;
    }

    public Map<String, String> getNameByLocale() {
        return Collections.unmodifiableMap(nameByLocale);
    }

    public Map<String, String> getDescriptionByLocale() {
        return Collections.unmodifiableMap(descriptionByLocale);
    }

    public Map<String, Object> getMetadataSchema() {
        return Collections.unmodifiableMap(metadataSchema);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Retorna um nome já resolvido pelo locale.
     */
    public String resolveName(String locale) {
        return TemplateI18n.resolve(nameByLocale, locale);
    }

    /**
     * Retorna uma descrição já resolvida pelo locale.
     */
    public String resolveDescription(String locale) {
        return TemplateI18n.resolve(descriptionByLocale, locale);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Template template = (Template) o;
        return Objects.equals(id, template.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class Builder {
        private UUID id;
        private UUID userId;
        private TemplateScope scope;
        private String code;
        private Map<String, String> nameByLocale = new HashMap<>();
        private Map<String, String> descriptionByLocale = new HashMap<>();
        private Map<String, Object> metadataSchema = new HashMap<>();
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

        public Builder scope(TemplateScope scope) {
            this.scope = scope;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder nameByLocale(Map<String, String> nameByLocale) {
            if (nameByLocale != null) {
                this.nameByLocale = new HashMap<>(nameByLocale);
            }
            return this;
        }

        public Builder descriptionByLocale(Map<String, String> descriptionByLocale) {
            if (descriptionByLocale != null) {
                this.descriptionByLocale = new HashMap<>(descriptionByLocale);
            }
            return this;
        }

        public Builder metadataSchema(Map<String, Object> metadataSchema) {
            if (metadataSchema != null) {
                this.metadataSchema = new HashMap<>(metadataSchema);
            }
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

        public Template build() {
            return new Template(this);
        }
    }
}
