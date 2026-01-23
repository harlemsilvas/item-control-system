package br.com.harlemsilvas.itemcontrol.api.infra.mongo.document;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.RuleType;
import br.com.harlemsilvas.itemcontrol.core.domain.valueobject.AlertSettings;
import br.com.harlemsilvas.itemcontrol.core.domain.valueobject.RuleCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

/**
 * Document MongoDB para Rule.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rules")
public class RuleDocument {

    @Id
    private String id;
    private String itemId;
    private String userId;
    private String name;
    private RuleType ruleType;
    private RuleCondition condition;
    private AlertSettings alertSettings;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;

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
