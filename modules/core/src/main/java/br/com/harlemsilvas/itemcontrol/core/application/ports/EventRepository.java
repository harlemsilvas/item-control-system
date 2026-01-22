package br.com.harlemsilvas.itemcontrol.core.application.ports;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.EventType;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Event;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port (interface) para persistência de Events.
 */
public interface EventRepository {

    /**
     * Salva um evento
     */
    Event save(Event event);

    /**
     * Busca um evento por ID
     */
    Optional<Event> findById(UUID id);

    /**
     * Busca todos os eventos de um item
     */
    List<Event> findByItemId(UUID itemId);

    /**
     * Busca eventos de um item por tipo
     */
    List<Event> findByItemIdAndEventType(UUID itemId, EventType eventType);

    /**
     * Busca eventos de um item em um período
     */
    List<Event> findByItemIdAndEventDateBetween(UUID itemId, Instant startDate, Instant endDate);

    /**
     * Busca eventos de um usuário
     */
    List<Event> findByUserId(UUID userId);

    /**
     * Busca últimos N eventos de um item
     */
    List<Event> findTopNByItemIdOrderByEventDateDesc(UUID itemId, int limit);

    /**
     * Busca o último evento de um item por tipo
     */
    Optional<Event> findLatestByItemIdAndEventType(UUID itemId, EventType eventType);

    /**
     * Deleta um evento por ID
     */
    void deleteById(UUID id);

    /**
     * Deleta todos os eventos de um item
     */
    void deleteByItemId(UUID itemId);

    /**
     * Conta eventos de um item
     */
    long countByItemId(UUID itemId);
}
