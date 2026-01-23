package br.com.harlemsilvas.itemcontrol.api.infra.mongo.document;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.AlertStatus;
import br.com.harlemsilvas.itemcontrol.core.domain.enums.AlertType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

/**
 * Documento MongoDB para Alert.
 * Representa alertas de items no banco de dados.
 */
@Document(collection = "alerts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertDocument {

    @Id
    private String id;
    private String ruleId;
    private String itemId;
    private String userId;
    private AlertType alertType;
    private String title;
    private String message;
    private Instant triggeredAt;
    private Instant dueAt;
    private AlertStatus status;
    private int priority;
    private Instant createdAt;
    private Instant readAt;
    private Instant completedAt;

    /**
     * Converte UUID para String para armazenamento no MongoDB.
     */
    public static String toStringId(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    /**
     * Converte String do MongoDB para UUID.
     */
    public static UUID toUUID(String id) {
        return id != null ? UUID.fromString(id) : null;
    }
}
