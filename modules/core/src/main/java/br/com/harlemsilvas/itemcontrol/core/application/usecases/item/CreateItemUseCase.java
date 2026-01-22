package br.com.harlemsilvas.itemcontrol.core.application.usecases.item;

import br.com.harlemsilvas.itemcontrol.core.application.ports.ItemRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Item;

import java.util.Objects;

/**
 * Use Case para criar um novo Item.
 */
public class CreateItemUseCase {

    private final ItemRepository itemRepository;

    public CreateItemUseCase(ItemRepository itemRepository) {
        this.itemRepository = Objects.requireNonNull(itemRepository, "ItemRepository cannot be null");
    }

    /**
     * Executa o caso de uso de criação de item.
     *
     * @param item Item a ser criado
     * @return Item criado com ID gerado
     */
    public Item execute(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        // Validações de negócio já estão no construtor da entidade
        return itemRepository.save(item);
    }
}
