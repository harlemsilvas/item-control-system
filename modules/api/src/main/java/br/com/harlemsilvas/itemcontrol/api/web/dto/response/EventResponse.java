package br.com.harlemsilvas.itemcontrol.api.web.dto.response;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * DTO para resposta de Event.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    private UUID id;
    private UUID itemId;
    private UUID userId;
    private EventType eventType;
    private Instant eventDate;
    private String description;
    private Map<String, Object> metrics;
    private Instant createdAt;
}
