package br.com.harlemsilvas.itemcontrol.api.web.dto.request;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.RuleType;
import br.com.harlemsilvas.itemcontrol.core.domain.valueobject.AlertSettings;
import br.com.harlemsilvas.itemcontrol.core.domain.valueobject.RuleCondition;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO para requisição de criação de Rule.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRuleRequest {

    @NotNull(message = "ItemId cannot be null")
    private UUID itemId;

    @NotNull(message = "UserId cannot be null")
    private UUID userId;

    @NotNull(message = "Name cannot be null")
    private String name;


    @NotNull(message = "RuleType cannot be null")
    private RuleType ruleType;

    @NotNull(message = "Condition cannot be null")
    private RuleCondition condition;

    @NotNull(message = "AlertSettings cannot be null")
    private AlertSettings alertSettings;

    private Boolean enabled;
}
