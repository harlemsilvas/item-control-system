package br.com.harlemsilvas.itemcontrol.api.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para request de atualização de categoria.
 *
 * @author Harlem Silva
 * @since 2026-01-23
 */
public record UpdateCategoryRequest(

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name
) {
}
