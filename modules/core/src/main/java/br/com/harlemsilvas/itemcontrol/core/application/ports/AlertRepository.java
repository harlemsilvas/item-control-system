package br.com.harlemsilvas.itemcontrol.core.application.ports;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.AlertStatus;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Alert;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port (interface) para persistência de Alerts.
 * Versão simplificada para MVP.
 */
public interface AlertRepository {

    /**
     * Salva um alerta
     */
    Alert save(Alert alert);

    /**
     * Busca um alerta por ID
     */
    Optional<Alert> findById(UUID id);

    /**
     * Busca todos os alertas de um usuário
     */
    List<Alert> findByUserId(UUID userId);

    /**
     * Busca alertas de um usuário por status
     */
    List<Alert> findByUserIdAndStatus(UUID userId, AlertStatus status);

    /**
     * Busca alertas de um item
     */
    List<Alert> findByItemId(UUID itemId);

    /**
     * Deleta um alerta por ID
     */
    void deleteById(UUID id);

    /**
     * Deleta todos os alertas de um item
     */
    void deleteByItemId(UUID itemId);

    /**
     * Conta alertas de um usuário por status
     */
    long countByUserIdAndStatus(UUID userId, AlertStatus status);
}
