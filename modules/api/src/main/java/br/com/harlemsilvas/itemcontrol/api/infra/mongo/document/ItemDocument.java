package br.com.harlemsilvas.itemcontrol.api.infra.mongo.document;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Documento MongoDB para Item.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "items")
public class ItemDocument {

    @Id
    private String id;
    private String userId;
    private String name;
    private String nickname;
    private String categoryId;
    private String templateCode;
    private ItemStatus status;
    private Set<String> tags;
    private Map<String, Object> metadata;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Converte um UUID para String.
     */
    public static String toStringId(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    /**
     * Converte uma String para UUID.
     */
    public static UUID toUUID(String id) {
        return id != null ? UUID.fromString(id) : null;
    }
}
