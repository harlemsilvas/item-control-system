package br.com.harlemsilvas.itemcontrol.api.web.dto.response;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.RuleType;
import br.com.harlemsilvas.itemcontrol.core.domain.valueobject.AlertSettings;
import br.com.harlemsilvas.itemcontrol.core.domain.valueobject.RuleCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO de resposta para Rule.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleResponse {

    private UUID id;
    private UUID itemId;
    private UUID userId;
    private String name;
    private RuleType ruleType;
    private RuleCondition condition;
    private AlertSettings alertSettings;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
}
