package br.com.harlemsilvas.itemcontrol.api.web.dto.request;
import br.com.harlemsilvas.itemcontrol.core.domain.enums.RuleType;
import br.com.harlemsilvas.itemcontrol.core.domain.valueobject.AlertSettings;
import br.com.harlemsilvas.itemcontrol.core.domain.valueobject.RuleCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRuleRequest {
    private String name;
    private RuleType ruleType;
    private RuleCondition condition;
    private AlertSettings alertSettings;
    private Boolean enabled;
}
