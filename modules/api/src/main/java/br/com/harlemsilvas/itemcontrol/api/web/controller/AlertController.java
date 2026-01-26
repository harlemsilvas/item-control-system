package br.com.harlemsilvas.itemcontrol.api.web.controller;

import br.com.harlemsilvas.itemcontrol.api.web.dto.request.CreateAlertRequest;
import br.com.harlemsilvas.itemcontrol.api.web.dto.response.AlertResponse;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.alert.AcknowledgeAlertUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.alert.CreateAlertUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.alert.ListPendingAlertsUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.alert.ResolveAlertUseCase;
import br.com.harlemsilvas.itemcontrol.core.domain.enums.AlertStatus;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Alert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller REST para gerenciamento de Alerts.
 */
@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
@Tag(name = "Alerts", description = "API para gerenciamento de alertas")
public class AlertController {

    private final CreateAlertUseCase createAlertUseCase;
    private final ListPendingAlertsUseCase listPendingAlertsUseCase;
    private final AcknowledgeAlertUseCase acknowledgeAlertUseCase;
    private final ResolveAlertUseCase resolveAlertUseCase;

    @PostMapping
    @Operation(summary = "Criar alerta", description = "Cria um novo alerta para um item")
    public ResponseEntity<AlertResponse> createAlert(@Valid @RequestBody CreateAlertRequest request) {
        Alert alert = new Alert.Builder()
                .itemId(request.getItemId())
                .userId(request.getUserId())
                .ruleId(request.getRuleId())
                .alertType(request.getAlertType())
                .title(request.getTitle())
                .message(request.getMessage())
                .triggeredAt(request.getTriggeredAt() != null ? request.getTriggeredAt() : Instant.now())
                .dueAt(request.getDueAt())
                .priority(request.getPriority() != null ? request.getPriority() : 3)
                .build();

        try {
            Alert created = createAlertUseCase.execute(alert);
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
        } catch (CreateAlertUseCase.ItemNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/pending")
    @Operation(summary = "Listar alertas pendentes", description = "Lista todos os alertas pendentes de um usuário ordenados por prioridade (ou todos se userId não fornecido)")
    public ResponseEntity<List<AlertResponse>> getPendingAlerts(
            @RequestParam(required = false) UUID userId) {
        // Se userId não fornecido, usar ID padrão para demo/desenvolvimento
        UUID effectiveUserId = userId != null ? userId : UUID.fromString("550e8400-e29b-41d4-a716-446655440001");

        List<Alert> alerts = listPendingAlertsUseCase.execute(effectiveUserId);
        List<AlertResponse> response = alerts.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar alertas por status", description = "Lista alertas de um usuário filtrados por status (ou todos se userId não fornecido)")
    public ResponseEntity<List<AlertResponse>> getAlertsByStatus(
            @RequestParam(required = false) UUID userId,
            @RequestParam AlertStatus status) {
        // Se userId não fornecido, usar ID padrão para demo/desenvolvimento
        UUID effectiveUserId = userId != null ? userId : UUID.fromString("550e8400-e29b-41d4-a716-446655440001");

        List<Alert> alerts = listPendingAlertsUseCase.executeByStatus(effectiveUserId, status);
        List<AlertResponse> response = alerts.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    @Operation(summary = "Contar alertas pendentes", description = "Retorna o número de alertas pendentes de um usuário (ou todos se userId não fornecido)")
    public ResponseEntity<Long> countPendingAlerts(
            @RequestParam(required = false) UUID userId) {
        // Se userId não fornecido, usar ID padrão para demo/desenvolvimento
        UUID effectiveUserId = userId != null ? userId : UUID.fromString("550e8400-e29b-41d4-a716-446655440001");

        long count = listPendingAlertsUseCase.countPending(effectiveUserId);
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{id}/acknowledge")
    @Operation(summary = "Reconhecer alerta", description = "Marca um alerta como reconhecido/lido")
    public ResponseEntity<AlertResponse> acknowledgeAlert(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID userId) {
        // Se userId não fornecido, usar ID padrão para demo/desenvolvimento
        UUID effectiveUserId = userId != null ? userId : UUID.fromString("550e8400-e29b-41d4-a716-446655440001");

        try {
            Alert alert = acknowledgeAlertUseCase.execute(id, effectiveUserId);
            return ResponseEntity.ok(toResponse(alert));
        } catch (AcknowledgeAlertUseCase.AlertNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/resolve")
    @Operation(summary = "Resolver alerta", description = "Marca um alerta como resolvido/concluído")
    public ResponseEntity<AlertResponse> resolveAlert(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID userId) {
        // Se userId não fornecido, usar ID padrão para demo/desenvolvimento
        UUID effectiveUserId = userId != null ? userId : UUID.fromString("550e8400-e29b-41d4-a716-446655440001");

        try {
            Alert alert = resolveAlertUseCase.execute(id, effectiveUserId);
            return ResponseEntity.ok(toResponse(alert));
        } catch (ResolveAlertUseCase.AlertNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Converte Alert para AlertResponse.
     */
    private AlertResponse toResponse(Alert alert) {
        return AlertResponse.builder()
                .id(alert.getId())
                .ruleId(alert.getRuleId())
                .itemId(alert.getItemId())
                .userId(alert.getUserId())
                .alertType(alert.getAlertType())
                .title(alert.getTitle())
                .message(alert.getMessage())
                .triggeredAt(alert.getTriggeredAt())
                .dueAt(alert.getDueAt())
                .status(alert.getStatus())
                .priority(alert.getPriority())
                .createdAt(alert.getCreatedAt())
                .readAt(alert.getReadAt())
                .completedAt(alert.getCompletedAt())
                .build();
    }
}
