package br.com.harlemsilvas.itemcontrol.api.adapters.persistence.mongodb.repository;

import br.com.harlemsilvas.itemcontrol.api.adapters.persistence.mongodb.document.CategoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository para CategoryDocument.
 *
 * @author Harlem Silva
 * @since 2026-01-23
 */
@Repository
public interface SpringDataCategoryRepository extends MongoRepository<CategoryDocument, String> {

    /**
     * Busca categorias por userId.
     *
     * @param userId o ID do usuário
     * @return lista de categorias do usuário
     */
    List<CategoryDocument> findByUserId(String userId);

    /**
     * Busca categorias raiz (sem pai) de um usuário.
     *
     * @param userId o ID do usuário
     * @return lista de categorias raiz
     */
    List<CategoryDocument> findByUserIdAndParentIdIsNull(String userId);

    /**
     * Busca categorias filhas de uma categoria pai.
     *
     * @param parentId o ID da categoria pai
     * @return lista de categorias filhas
     */
    List<CategoryDocument> findByParentId(String parentId);

    /**
     * Deleta todas as categorias de um usuário.
     *
     * @param userId o ID do usuário
     */
    void deleteByUserId(String userId);

    /**
     * Verifica se existem categorias filhas.
     *
     * @param parentId o ID da categoria pai
     * @return true se existe, false caso contrário
     */
    boolean existsByParentId(String parentId);

    /**
     * Conta categorias de um usuário.
     *
     * @param userId o ID do usuário
     * @return número de categorias
     */
    long countByUserId(String userId);
}
