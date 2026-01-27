package br.com.harlemsilvas.itemcontrol.api.infra.mongo.mapper;

import br.com.harlemsilvas.itemcontrol.api.infra.mongo.document.TemplateDocument;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Template;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Mapper para converter entre Template (domínio) e TemplateDocument (MongoDB).
 */
@Component
public class TemplateDocumentMapper {

    public TemplateDocument toDocument(Template template) {
        if (template == null) {
            return null;
        }

        return TemplateDocument.builder()
            .id(template.getId() != null ? template.getId().toString() : null)
            .userId(template.getUserId() != null ? template.getUserId().toString() : null)
            .scope(template.getScope())
            .code(template.getCode())
            .nameByLocale(template.getNameByLocale())
            .descriptionByLocale(template.getDescriptionByLocale())
            .metadataSchema(template.getMetadataSchema())
            .createdAt(template.getCreatedAt())
            .updatedAt(template.getUpdatedAt())
            .build();
    }

    public Template toDomain(TemplateDocument document) {
        if (document == null) {
            return null;
        }

        return new Template.Builder()
            .id(document.getId() != null ? UUID.fromString(document.getId()) : null)
            .userId(document.getUserId() != null ? UUID.fromString(document.getUserId()) : null)
            .scope(document.getScope())
            .code(document.getCode())
            .nameByLocale(document.getNameByLocale())
            .descriptionByLocale(document.getDescriptionByLocale())
            .metadataSchema(document.getMetadataSchema())
            .createdAt(document.getCreatedAt())
            .updatedAt(document.getUpdatedAt())
            .build();
    }
}
