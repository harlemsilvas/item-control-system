package br.com.harlemsilvas.itemcontrol.core.domain.service;

import br.com.harlemsilvas.itemcontrol.core.domain.model.Template;

import java.util.*;

/**
 * Validação simples (e evolutiva) de metadata contra o metadataSchema do Template.
 *
 * O schema é representado como Map<String, Object>, onde cada campo pode ser:
 * - um Map com chaves conhecidas: { type: "string|number|boolean|object|array", required: true|false }
 * - outros formatos são ignorados (para manter compatibilidade), mas podem ser evoluídos depois.
 */
public final class MetadataSchemaValidator {

    private MetadataSchemaValidator() {
    }

    public static void validateOrThrow(Template template, Map<String, Object> metadata) {
        if (template == null) {
            throw new IllegalArgumentException("Template cannot be null");
        }

        Map<String, Object> schema = template.getMetadataSchema();
        if (schema == null || schema.isEmpty()) {
            // Sem schema: não valida (compat)
            return;
        }

        Map<String, Object> safeMetadata = metadata != null ? metadata : Collections.emptyMap();

        // 1) required
        for (Map.Entry<String, Object> entry : schema.entrySet()) {
            String field = entry.getKey();
            Map<String, Object> fieldSchema = asMap(entry.getValue());
            if (fieldSchema == null) {
                continue;
            }

            boolean required = asBoolean(fieldSchema.get("required"));
            if (required && !safeMetadata.containsKey(field)) {
                throw new MetadataSchemaValidationException("Missing required metadata field: " + field);
            }
        }

        // 2) type checks (apenas se type estiver definido)
        for (Map.Entry<String, Object> entry : schema.entrySet()) {
            String field = entry.getKey();
            if (!safeMetadata.containsKey(field)) {
                continue;
            }

            Map<String, Object> fieldSchema = asMap(entry.getValue());
            if (fieldSchema == null) {
                continue;
            }

            String type = asString(fieldSchema.get("type"));
            if (type == null || type.isBlank()) {
                continue;
            }

            Object value = safeMetadata.get(field);
            if (value == null) {
                // null aceito (por enquanto) — required garante presença
                continue;
            }

            if (!matchesType(type, value)) {
                throw new MetadataSchemaValidationException(
                    "Invalid metadata type for field '%s'. Expected '%s' but got '%s'".formatted(
                        field,
                        type,
                        value.getClass().getSimpleName()
                    )
                );
            }
        }
    }

    private static boolean matchesType(String type, Object value) {
        return switch (type.toLowerCase(Locale.ROOT)) {
            case "string" -> value instanceof String;
            case "number" -> value instanceof Number;
            case "boolean" -> value instanceof Boolean;
            case "object" -> value instanceof Map;
            case "array" -> value instanceof List || value.getClass().isArray();
            default -> true; // tipo desconhecido: não bloqueia
        };
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(Object o) {
        if (o instanceof Map<?, ?> m) {
            Map<String, Object> result = new HashMap<>();
            for (Map.Entry<?, ?> e : m.entrySet()) {
                if (e.getKey() != null) {
                    result.put(e.getKey().toString(), e.getValue());
                }
            }
            return result;
        }
        return null;
    }

    private static boolean asBoolean(Object o) {
        if (o instanceof Boolean b) {
            return b;
        }
        if (o instanceof String s) {
            return Boolean.parseBoolean(s);
        }
        return false;
    }

    private static String asString(Object o) {
        return o == null ? null : o.toString();
    }

    public static class MetadataSchemaValidationException extends RuntimeException {
        public MetadataSchemaValidationException(String message) {
            super(message);
        }
    }
}
