package br.com.harlemsilvas.itemcontrol.api.dto.category;

import br.com.harlemsilvas.itemcontrol.core.domain.model.Category;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO para response de categoria.
 *
 * @author Harlem Silva
 * @since 2026-01-23
 */
public record CategoryResponse(
        UUID id,
        UUID userId,
        String name,
        UUID parentId,
        Instant createdAt,
        Instant updatedAt
) {

    /**
     * Converte uma entidade Category para CategoryResponse.
     *
     * @param category a categoria a ser convertida
     * @return o DTO de response
     */
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getUserId(),
                category.getName(),
                category.getParentId(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
