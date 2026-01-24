package br.com.harlemsilvas.itemcontrol.api.adapters.persistence.mongodb.mapper;

import br.com.harlemsilvas.itemcontrol.api.adapters.persistence.mongodb.document.CategoryDocument;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Category;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Mapper para conversão entre Category e CategoryDocument.
 *
 * @author Harlem Silva
 * @since 2026-01-23
 */
@Component
public class CategoryDocumentMapper {

    /**
     * Converte Category (domain) para CategoryDocument (MongoDB).
     *
     * @param category a entidade de domínio
     * @return o document MongoDB
     */
    public CategoryDocument toDocument(Category category) {
        if (category == null) {
            return null;
        }

        return new CategoryDocument(
                category.getId() != null ? category.getId().toString() : null,
                category.getUserId() != null ? category.getUserId().toString() : null,
                category.getName(),
                category.getParentId() != null ? category.getParentId().toString() : null,
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    /**
     * Converte CategoryDocument (MongoDB) para Category (domain).
     *
     * @param document o document MongoDB
     * @return a entidade de domínio
     */
    public Category toDomain(CategoryDocument document) {
        if (document == null) {
            return null;
        }

        return new Category.Builder()
                .id(document.getId() != null ? UUID.fromString(document.getId()) : null)
                .userId(document.getUserId() != null ? UUID.fromString(document.getUserId()) : null)
                .name(document.getName())
                .parentId(document.getParentId() != null ? UUID.fromString(document.getParentId()) : null)
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
}
