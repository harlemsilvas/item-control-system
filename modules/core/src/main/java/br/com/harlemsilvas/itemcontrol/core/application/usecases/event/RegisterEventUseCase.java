package br.com.harlemsilvas.itemcontrol.core.application.usecases.event;

import br.com.harlemsilvas.itemcontrol.core.application.ports.EventRepository;
import br.com.harlemsilvas.itemcontrol.core.application.ports.ItemRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Event;

import java.util.Objects;

/**
 * Use Case para registrar um novo evento.
 */
public class RegisterEventUseCase {

    private final EventRepository eventRepository;
    private final ItemRepository itemRepository;

    public RegisterEventUseCase(EventRepository eventRepository, ItemRepository itemRepository) {
        this.eventRepository = Objects.requireNonNull(eventRepository, "EventRepository cannot be null");
        this.itemRepository = Objects.requireNonNull(itemRepository, "ItemRepository cannot be null");
    }

    /**
     * Executa o caso de uso de registro de evento.
     *
     * @param event Evento a ser registrado
     * @return Evento registrado
     * @throws ItemNotFoundException se o item associado não existir
     */
    public Event execute(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }

        // Verifica se o item existe
        if (!itemRepository.existsById(event.getItemId())) {
            throw new ItemNotFoundException("Item not found with id: " + event.getItemId());
        }

        return eventRepository.save(event);
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
