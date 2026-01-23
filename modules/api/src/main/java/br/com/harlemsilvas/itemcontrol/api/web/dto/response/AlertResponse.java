package br.com.harlemsilvas.itemcontrol.api.web.dto.response;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.AlertStatus;
import br.com.harlemsilvas.itemcontrol.core.domain.enums.AlertType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO de resposta para Alert.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponse {

    private UUID id;
    private UUID ruleId;
    private UUID itemId;
    private UUID userId;
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
}
