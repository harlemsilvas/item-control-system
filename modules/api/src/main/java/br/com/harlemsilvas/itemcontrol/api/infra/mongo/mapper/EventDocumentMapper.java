package br.com.harlemsilvas.itemcontrol.api.infra.mongo.mapper;

import br.com.harlemsilvas.itemcontrol.api.infra.mongo.document.EventDocument;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Event;
import org.springframework.stereotype.Component;

/**
 * Mapper para converter entre Event (domínio) e EventDocument (MongoDB).
 */
@Component
public class EventDocumentMapper {

    /**
     * Converte entidade de domínio para documento MongoDB.
     */
    public EventDocument toDocument(Event event) {
        if (event == null) {
            return null;
        }

        return EventDocument.builder()
            .id(EventDocument.toStringId(event.getId()))
            .itemId(EventDocument.toStringId(event.getItemId()))
            .userId(EventDocument.toStringId(event.getUserId()))
            .eventType(event.getEventType())
            .eventDate(event.getEventDate())
            .description(event.getDescription())
            .metrics(event.getMetrics())
            .createdAt(event.getCreatedAt())
            .build();
    }

    /**
     * Converte documento MongoDB para entidade de domínio.
     */
    public Event toDomain(EventDocument document) {
        if (document == null) {
            return null;
        }

        return new Event.Builder()
            .id(EventDocument.toUUID(document.getId()))
            .itemId(EventDocument.toUUID(document.getItemId()))
            .userId(EventDocument.toUUID(document.getUserId()))
            .eventType(document.getEventType())
            .eventDate(document.getEventDate())
            .description(document.getDescription())
            .metrics(document.getMetrics())
            .createdAt(document.getCreatedAt())
            .build();
    }
}
