package br.com.harlemsilvas.itemcontrol.core.application.ports;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.ItemStatus;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Item;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Port (interface) para persistência de Items.
 * A implementação será feita nos módulos externos (api/worker).
 */
public interface ItemRepository {

    /**
     * Salva um item (create ou update)
     */
    Item save(Item item);

    /**
     * Busca um item por ID
     */
    Optional<Item> findById(UUID id);

    /**
     * Busca todos os itens de um usuário
     */
    List<Item> findByUserId(UUID userId);

    /**
     * Busca itens por usuário e status
     */
    List<Item> findByUserIdAndStatus(UUID userId, ItemStatus status);

    /**
     * Busca itens por usuário e template
     */
    List<Item> findByUserIdAndTemplateCode(UUID userId, String templateCode);

    /**
     * Busca itens por usuário e tags
     */
    List<Item> findByUserIdAndTags(UUID userId, Set<String> tags);

    /**
     * Busca itens por categoria
     */
    List<Item> findByCategoryId(UUID categoryId);

    /**
     * Deleta um item por ID
     */
    void deleteById(UUID id);

    /**
     * Verifica se um item existe
     */
    boolean existsById(UUID id);

    /**
     * Conta itens por usuário
     */
    long countByUserId(UUID userId);
}
