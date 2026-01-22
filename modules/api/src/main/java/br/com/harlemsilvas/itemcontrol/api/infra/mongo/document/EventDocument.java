package br.com.harlemsilvas.itemcontrol.api.infra.mongo.document;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Documento MongoDB para Event.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "events")
public class EventDocument {

    @Id
    private String id;
    private String itemId;
    private String userId;
    private EventType eventType;
    private Instant eventDate;
    private String description;
    private Map<String, Object> metrics;
    private Instant createdAt;

    public static String toStringId(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    public static UUID toUUID(String id) {
        return id != null ? UUID.fromString(id) : null;
    }
}
