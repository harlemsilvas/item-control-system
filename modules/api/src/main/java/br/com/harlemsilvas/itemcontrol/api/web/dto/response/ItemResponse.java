package br.com.harlemsilvas.itemcontrol.api.web.dto.response;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * DTO para resposta de Item.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {

    private UUID id;
    private UUID userId;
    private String name;
    private String nickname;
    private UUID categoryId;
    private String templateCode;
    private ItemStatus status;
    private Set<String> tags;
    private Map<String, Object> metadata;
    private Instant createdAt;
    private Instant updatedAt;
}
