package br.com.harlemsilvas.itemcontrol.core.usecases.category;

import br.com.harlemsilvas.itemcontrol.core.application.ports.CategoryRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Category;

import java.util.Objects;
import java.util.UUID;

/**
 * Use case para atualizar uma categoria existente.
 *
 * @author Harlem Silva
 * @since 2026-01-23
 */
public class UpdateCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public UpdateCategoryUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository, "CategoryRepository cannot be null");
    }

    /**
     * Executa a atualização de uma categoria.
     *
     * @param id o ID da categoria a ser atualizada
     * @param newName o novo nome da categoria
     * @return a categoria atualizada
     * @throws IllegalArgumentException se id ou newName forem null
     * @throws CategoryNotFoundException se a categoria não for encontrada
     */
    public Category execute(UUID id, String newName) {
        Objects.requireNonNull(id, "Category ID cannot be null");
        Objects.requireNonNull(newName, "New name cannot be null");

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        // Usa o método de negócio da entidade para atualizar
        existingCategory.updateName(newName);

        return categoryRepository.save(existingCategory);
    }
    
    /**
     * Exception lançada quando uma categoria não é encontrada.
     */
    public static class CategoryNotFoundException extends RuntimeException {
        public CategoryNotFoundException(String message) {
            super(message);
        }
    }
}
