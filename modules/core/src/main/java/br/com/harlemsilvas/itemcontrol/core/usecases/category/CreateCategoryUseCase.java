package br.com.harlemsilvas.itemcontrol.core.usecases.category;

import br.com.harlemsilvas.itemcontrol.core.application.ports.CategoryRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Category;

import java.util.Objects;

/**
 * Use case para criar uma nova categoria.
 *
 * @author Harlem Silva
 * @since 2026-01-23
 */
public class CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public CreateCategoryUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository, "CategoryRepository cannot be null");
    }

    /**
     * Executa a criação de uma nova categoria.
     *
     * @param category a categoria a ser criada
     * @return a categoria criada com ID gerado
     * @throws IllegalArgumentException se category for null
     */
    public Category execute(Category category) {
        Objects.requireNonNull(category, "Category cannot be null");

        return categoryRepository.save(category);
    }
}
