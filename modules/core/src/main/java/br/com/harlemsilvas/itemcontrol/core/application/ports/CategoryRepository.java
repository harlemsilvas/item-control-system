package br.com.harlemsilvas.itemcontrol.core.application.ports;

import br.com.harlemsilvas.itemcontrol.core.domain.model.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port (interface) para persistência de Categories.
 */
public interface CategoryRepository {

    /**
     * Salva uma categoria
     */
    Category save(Category category);

    /**
     * Busca uma categoria por ID
     */
    Optional<Category> findById(UUID id);

    /**
     * Busca todas as categorias de um usuário
     */
    List<Category> findByUserId(UUID userId);

    /**
     * Busca categorias raiz (sem pai) de um usuário
     */
    List<Category> findRootByUserId(UUID userId);

    /**
     * Busca categorias filhas de uma categoria pai
     */
    List<Category> findByParentId(UUID parentId);

    /**
     * Deleta uma categoria por ID
     */
    void deleteById(UUID id);

    /**
     * Verifica se uma categoria existe
     */
    boolean existsById(UUID id);

    /**
     * Verifica se existem categorias filhas
     */
    boolean hasChildren(UUID categoryId);

    /**
     * Conta categorias de um usuário
     */
    long countByUserId(UUID userId);
}
