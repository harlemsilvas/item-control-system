package br.com.harlemsilvas.itemcontrol.core.application.usecases.alert;

import br.com.harlemsilvas.itemcontrol.core.application.ports.AlertRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Alert;

import java.util.Objects;
import java.util.UUID;

/**
 * Use Case para resolver um alerta (marcar como concluído).
 */
public class ResolveAlertUseCase {

    private final AlertRepository alertRepository;

    public ResolveAlertUseCase(AlertRepository alertRepository) {
        this.alertRepository = Objects.requireNonNull(alertRepository, "AlertRepository cannot be null");
    }

    /**
     * Resolve um alerta, marcando como COMPLETED.
     *
     * @param alertId ID do alerta
     * @param userId ID do usuário que está resolvendo (não usado na entidade atual)
     * @return Alerta atualizado
     * @throws IllegalArgumentException se os parâmetros forem nulos
     * @throws AlertNotFoundException se o alerta não existir
     */
    public Alert execute(UUID alertId, UUID userId) {
        if (alertId == null) {
            throw new IllegalArgumentException("AlertId cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new AlertNotFoundException("Alert not found with id: " + alertId));

        // Usar o método de negócio da entidade
        alert.complete();

        return alertRepository.save(alert);
    }

    /**
     * Exception lançada quando um alerta não é encontrado.
     */
    public static class AlertNotFoundException extends RuntimeException {
        public AlertNotFoundException(String message) {
            super(message);
        }
    }
}
