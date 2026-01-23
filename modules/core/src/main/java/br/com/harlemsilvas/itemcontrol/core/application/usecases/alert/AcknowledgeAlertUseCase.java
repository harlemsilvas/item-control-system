package br.com.harlemsilvas.itemcontrol.core.application.usecases.alert;

import br.com.harlemsilvas.itemcontrol.core.application.ports.AlertRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Alert;

import java.util.Objects;
import java.util.UUID;

/**
 * Use Case para reconhecer um alerta (marcar como lido).
 */
public class AcknowledgeAlertUseCase {

    private final AlertRepository alertRepository;

    public AcknowledgeAlertUseCase(AlertRepository alertRepository) {
        this.alertRepository = Objects.requireNonNull(alertRepository, "AlertRepository cannot be null");
    }

    /**
     * Reconhece um alerta, marcando como READ.
     *
     * @param alertId ID do alerta
     * @param userId ID do usuário que está reconhecendo (não usado na entidade atual)
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
        alert.markAsRead();

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
