package br.com.harlemsilvas.itemcontrol.core.usecases.category;

import br.com.harlemsilvas.itemcontrol.core.application.ports.CategoryRepository;

import java.util.Objects;
import java.util.UUID;

/**
 * Use case para deletar uma categoria.
 *
 * @author Harlem Silva
 * @since 2026-01-23
 */
public class DeleteCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public DeleteCategoryUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository, "CategoryRepository cannot be null");
    }

    /**
     * Executa a deleção de uma categoria.
     *
     * @param id o ID da categoria a ser deletada
     * @throws IllegalArgumentException se id for null
     * @throws CategoryNotFoundException se a categoria não for encontrada
     */
    public void execute(UUID id) {
        Objects.requireNonNull(id, "Category ID cannot be null");

        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException("Category not found with id: " + id);
        }

        categoryRepository.deleteById(id);
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
