package br.com.harlemsilvas.itemcontrol.api.web.dto.request;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.AlertType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO para requisição de criação de Alert.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAlertRequest {

    @NotNull(message = "ItemId cannot be null")
    private UUID itemId;

    @NotNull(message = "UserId cannot be null")
    private UUID userId;

    @NotNull(message = "RuleId cannot be null")
    private UUID ruleId;

    @NotNull(message = "AlertType cannot be null")
    private AlertType alertType;

    @NotNull(message = "Title cannot be null")
    private String title;

    @NotNull(message = "Message cannot be null")
    private String message;

    private Instant triggeredAt;

    private Instant dueAt;

    @Min(value = 1, message = "Priority must be between 1 and 5")
    @Max(value = 5, message = "Priority must be between 1 and 5")
    private Integer priority;
}
