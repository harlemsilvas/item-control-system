package br.com.harlemsilvas.itemcontrol.core.application.usecases.event;

import br.com.harlemsilvas.itemcontrol.core.application.ports.EventRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Event;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Use Case para obter histórico de eventos de um item.
 */
public class GetEventHistoryUseCase {

    private final EventRepository eventRepository;

    public GetEventHistoryUseCase(EventRepository eventRepository) {
        this.eventRepository = Objects.requireNonNull(eventRepository, "EventRepository cannot be null");
    }

    /**
     * Executa o caso de uso de obtenção de histórico de eventos.
     *
     * @param itemId ID do item
     * @return Lista de eventos do item
     */
    public List<Event> execute(UUID itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("ItemId cannot be null");
        }

        return eventRepository.findByItemId(itemId);
    }

    /**
     * Obtém os últimos N eventos de um item.
     *
     * @param itemId ID do item
     * @param limit Número máximo de eventos
     * @return Lista dos últimos eventos
     */
    public List<Event> executeWithLimit(UUID itemId, int limit) {
        if (itemId == null) {
            throw new IllegalArgumentException("ItemId cannot be null");
        }
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be positive");
        }

        return eventRepository.findTopNByItemIdOrderByEventDateDesc(itemId, limit);
    }
}
