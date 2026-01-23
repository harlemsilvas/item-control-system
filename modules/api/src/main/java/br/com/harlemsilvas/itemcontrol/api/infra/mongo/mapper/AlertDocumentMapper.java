package br.com.harlemsilvas.itemcontrol.api.infra.mongo.mapper;

import br.com.harlemsilvas.itemcontrol.api.infra.mongo.document.AlertDocument;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Alert;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Mapper para converter entre Alert (domínio) e AlertDocument (MongoDB).
 */
@Component
public class AlertDocumentMapper {

    /**
     * Converte entidade de domínio para documento MongoDB.
     */
    public AlertDocument toDocument(Alert alert) {
        if (alert == null) {
            return null;
        }

        return AlertDocument.builder()
                .id(AlertDocument.toStringId(alert.getId()))
                .ruleId(AlertDocument.toStringId(alert.getRuleId()))
                .itemId(AlertDocument.toStringId(alert.getItemId()))
                .userId(AlertDocument.toStringId(alert.getUserId()))
                .alertType(alert.getAlertType())
                .title(alert.getTitle())
                .message(alert.getMessage())
                .triggeredAt(alert.getTriggeredAt())
                .dueAt(alert.getDueAt())
                .status(alert.getStatus())
                .priority(alert.getPriority())
                .createdAt(alert.getCreatedAt() != null ? alert.getCreatedAt() : Instant.now())
                .readAt(alert.getReadAt())
                .completedAt(alert.getCompletedAt())
                .build();
    }

    /**
     * Converte documento MongoDB para entidade de domínio.
     */
    public Alert toDomain(AlertDocument document) {
        if (document == null) {
            return null;
        }

        return new Alert.Builder()
                .id(AlertDocument.toUUID(document.getId()))
                .ruleId(AlertDocument.toUUID(document.getRuleId()))
                .itemId(AlertDocument.toUUID(document.getItemId()))
                .userId(AlertDocument.toUUID(document.getUserId()))
                .alertType(document.getAlertType())
                .title(document.getTitle())
                .message(document.getMessage())
                .triggeredAt(document.getTriggeredAt())
                .dueAt(document.getDueAt())
                .status(document.getStatus())
                .priority(document.getPriority())
                .createdAt(document.getCreatedAt())
                .readAt(document.getReadAt())
                .completedAt(document.getCompletedAt())
                .build();
    }
}
