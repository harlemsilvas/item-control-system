package br.com.harlemsilvas.itemcontrol.api.web.dto.request;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * DTO para requisição de registro de Event.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEventRequest {

    @NotNull(message = "ItemId cannot be null")
    private UUID itemId;

    @NotNull(message = "UserId cannot be null")
    private UUID userId;

    @NotNull(message = "EventType cannot be null")
    private EventType eventType;

    @NotNull(message = "EventDate cannot be null")
    private Instant eventDate;

    private String description;

    private Map<String, Object> metrics;
}
