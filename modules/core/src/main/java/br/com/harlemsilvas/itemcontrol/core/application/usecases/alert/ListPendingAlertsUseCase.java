package br.com.harlemsilvas.itemcontrol.core.application.usecases.alert;

import br.com.harlemsilvas.itemcontrol.core.application.ports.AlertRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.enums.AlertStatus;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Alert;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use Case para listar alertas pendentes de um usuário.
 */
public class ListPendingAlertsUseCase {

    private final AlertRepository alertRepository;

    public ListPendingAlertsUseCase(AlertRepository alertRepository) {
        this.alertRepository = Objects.requireNonNull(alertRepository, "AlertRepository cannot be null");
    }

    /**
     * Lista todos os alertas pendentes de um usuário, ordenados por prioridade e data de vencimento.
     *
     * @param userId ID do usuário
     * @return Lista de alertas pendentes ordenados
     * @throws IllegalArgumentException se userId for nulo
     */
    public List<Alert> execute(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        // Buscar alertas pendentes e ordenar por prioridade (maior primeiro) e dueAt
        List<Alert> alerts = alertRepository.findByUserIdAndStatus(userId, AlertStatus.PENDING);

        return alerts.stream()
                .sorted((a1, a2) -> {
                    // Ordenar por prioridade DESC (maior primeiro)
                    int priorityCompare = Integer.compare(a2.getPriority(), a1.getPriority());
                    if (priorityCompare != 0) {
                        return priorityCompare;
                    }
                    // Se prioridades iguais, ordenar por dueAt ASC
                    if (a1.getDueAt() == null && a2.getDueAt() == null) return 0;
                    if (a1.getDueAt() == null) return 1;
                    if (a2.getDueAt() == null) return -1;
                    return a1.getDueAt().compareTo(a2.getDueAt());
                })
                .collect(Collectors.toList());
    }

    /**
     * Lista alertas de um usuário por status específico.
     *
     * @param userId ID do usuário
     * @param status Status dos alertas
     * @return Lista de alertas com o status especificado
     */
    public List<Alert> executeByStatus(UUID userId, AlertStatus status) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        return alertRepository.findByUserIdAndStatus(userId, status);
    }

    /**
     * Conta quantos alertas pendentes um usuário possui.
     *
     * @param userId ID do usuário
     * @return Número de alertas pendentes
     */
    public long countPending(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        return alertRepository.countByUserIdAndStatus(userId, AlertStatus.PENDING);
    }
}
