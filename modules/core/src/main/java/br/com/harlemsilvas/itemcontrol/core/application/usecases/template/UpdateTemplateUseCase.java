package br.com.harlemsilvas.itemcontrol.core.application.usecases.template;

import br.com.harlemsilvas.itemcontrol.core.application.ports.TemplateRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Template;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Use Case para atualizar um Template.
 */
public class UpdateTemplateUseCase {

    private final TemplateRepository templateRepository;

    public UpdateTemplateUseCase(TemplateRepository templateRepository) {
        this.templateRepository = Objects.requireNonNull(templateRepository, "TemplateRepository cannot be null");
    }

    /**
     * Atualiza nome/descrição/schema de um template existente.
     *
     * Observação: não permite trocar scope/userId/code.
     */
    public Template execute(UUID templateId, Template updatedData) {
        if (templateId == null) {
            throw new IllegalArgumentException("TemplateId cannot be null");
        }
        if (updatedData == null) {
            throw new IllegalArgumentException("Updated template cannot be null");
        }

        Template existing = templateRepository.findById(templateId)
            .orElseThrow(() -> new TemplateNotFoundException(templateId));

        Template merged = new Template.Builder()
            .id(existing.getId())
            .scope(existing.getScope())
            .userId(existing.getUserId())
            .code(existing.getCode())
            .nameByLocale(updatedData.getNameByLocale())
            .descriptionByLocale(updatedData.getDescriptionByLocale())
            .metadataSchema(updatedData.getMetadataSchema())
            .createdAt(existing.getCreatedAt())
            .updatedAt(Instant.now())
            .build();

        return templateRepository.save(merged);
    }

    public static class TemplateNotFoundException extends RuntimeException {
        public TemplateNotFoundException(UUID id) {
            super("Template not found: " + id);
        }
    }
}
