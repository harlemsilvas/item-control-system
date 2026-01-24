package br.com.harlemsilvas.itemcontrol.api.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * DTO para request de criação de categoria.
 *
 * @author Harlem Silva
 * @since 2026-01-23
 */
public record CreateCategoryRequest(

        @NotNull(message = "User ID is required")
        UUID userId,

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        UUID parentId
) {
}
