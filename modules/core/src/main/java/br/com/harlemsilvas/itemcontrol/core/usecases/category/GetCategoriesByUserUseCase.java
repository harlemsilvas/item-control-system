package br.com.harlemsilvas.itemcontrol.core.usecases.category;

import br.com.harlemsilvas.itemcontrol.core.application.ports.CategoryRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Category;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Use case para buscar categorias por usuário.
 *
 * @author Harlem Silva
 * @since 2026-01-23
 */
public class GetCategoriesByUserUseCase {

    private final CategoryRepository categoryRepository;

    public GetCategoriesByUserUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository, "CategoryRepository cannot be null");
    }

    /**
     * Executa a busca de categorias por usuário.
     *
     * @param userId o ID do usuário
     * @return lista de categorias do usuário
     * @throws IllegalArgumentException se userId for null
     */
    public List<Category> execute(UUID userId) {
        Objects.requireNonNull(userId, "UserId cannot be null");

        return categoryRepository.findByUserId(userId);
    }
}
