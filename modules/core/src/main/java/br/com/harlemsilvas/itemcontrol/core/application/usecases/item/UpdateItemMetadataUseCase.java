package br.com.harlemsilvas.itemcontrol.core.application.usecases.item;

import br.com.harlemsilvas.itemcontrol.core.application.ports.ItemRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Item;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Use Case para atualizar metadata de um Item.
 */
public class UpdateItemMetadataUseCase {

    private final ItemRepository itemRepository;

    public UpdateItemMetadataUseCase(ItemRepository itemRepository) {
        this.itemRepository = Objects.requireNonNull(itemRepository, "ItemRepository cannot be null");
    }

    /**
     * Executa o caso de uso de atualização de metadata.
     *
     * @param itemId ID do item
     * @param metadata Novos metadados
     * @return Item atualizado
     * @throws ItemNotFoundException se o item não for encontrado
     */
    public Item execute(UUID itemId, Map<String, Object> metadata) {
        if (itemId == null) {
            throw new IllegalArgumentException("ItemId cannot be null");
        }
        if (metadata == null || metadata.isEmpty()) {
            throw new IllegalArgumentException("Metadata cannot be null or empty");
        }

        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + itemId));

        item.updateMetadata(metadata);

        return itemRepository.save(item);
    }

    /**
     * Exception lançada quando um item não é encontrado.
     */
    public static class ItemNotFoundException extends RuntimeException {
        public ItemNotFoundException(String message) {
            super(message);
        }
    }
}
