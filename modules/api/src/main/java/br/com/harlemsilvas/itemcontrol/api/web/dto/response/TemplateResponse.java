package br.com.harlemsilvas.itemcontrol.api.web.dto.response;

import br.com.harlemsilvas.itemcontrol.core.domain.model.TemplateScope;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * DTO de resposta para Template.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateResponse {

    private UUID id;

    private UUID userId;

    private TemplateScope scope;

    private String code;

    private Map<String, String> nameByLocale;

    private Map<String, String> descriptionByLocale;

    private Map<String, Object> metadataSchema;

    /** Nome resolvido pelo locale (opcional) */
    private String name;

    /** Descrição resolvida pelo locale (opcional) */
    private String description;

    private Instant createdAt;

    private Instant updatedAt;
}
