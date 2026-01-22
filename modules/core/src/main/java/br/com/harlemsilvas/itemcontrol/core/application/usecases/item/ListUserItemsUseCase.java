package br.com.harlemsilvas.itemcontrol.core.application.usecases.item;

import br.com.harlemsilvas.itemcontrol.core.application.ports.ItemRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.enums.ItemStatus;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Item;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Use Case para listar itens de um usuário.
 */
public class ListUserItemsUseCase {

    private final ItemRepository itemRepository;

    public ListUserItemsUseCase(ItemRepository itemRepository) {
        this.itemRepository = Objects.requireNonNull(itemRepository, "ItemRepository cannot be null");
    }

    /**
     * Executa o caso de uso de listagem de itens por usuário.
     *
     * @param userId ID do usuário
     * @return Lista de itens do usuário
     */
    public List<Item> execute(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        return itemRepository.findByUserId(userId);
    }

    /**
     * Lista itens ativos de um usuário.
     *
     * @param userId ID do usuário
     * @return Lista de itens ativos
     */
    public List<Item> executeActiveOnly(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        return itemRepository.findByUserIdAndStatus(userId, ItemStatus.ACTIVE);
    }
}
