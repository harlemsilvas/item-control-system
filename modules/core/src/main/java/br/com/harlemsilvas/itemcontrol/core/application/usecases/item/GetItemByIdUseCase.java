package br.com.harlemsilvas.itemcontrol.core.application.usecases.item;

import br.com.harlemsilvas.itemcontrol.core.application.ports.ItemRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Item;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Use Case para buscar um Item por ID.
 */
public class GetItemByIdUseCase {

    private final ItemRepository itemRepository;

    public GetItemByIdUseCase(ItemRepository itemRepository) {
        this.itemRepository = Objects.requireNonNull(itemRepository, "ItemRepository cannot be null");
    }

    /**
     * Executa o caso de uso de busca de item por ID.
     *
     * @param itemId ID do item
     * @return Optional contendo o item se encontrado
     */
    public Optional<Item> execute(UUID itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("ItemId cannot be null");
        }

        return itemRepository.findById(itemId);
    }
}
