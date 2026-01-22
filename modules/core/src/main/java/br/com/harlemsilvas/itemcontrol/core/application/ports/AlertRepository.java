package br.com.harlemsilvas.itemcontrol.core.application.ports;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.AlertStatus;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Alert;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port (interface) para persistência de Alerts.
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
     * Busca alertas pendentes de um usuário
     */
    List<Alert> findPendingByUserId(UUID userId);

    /**
     * Busca alertas de um item
     */
    List<Alert> findByItemId(UUID itemId);

    /**
     * Busca alertas de uma regra
     */
    List<Alert> findByRuleId(UUID ruleId);

    /**
     * Busca alertas vencidos (overdue)
     */
    List<Alert> findOverdueByUserId(UUID userId, Instant now);

    /**
     * Busca alertas por prioridade
     */
    List<Alert> findByUserIdAndPriorityGreaterThanEqual(UUID userId, int priority);

    /**
     * Deleta um alerta por ID
     */
    void deleteById(UUID id);

    /**
     * Deleta alertas antigos (limpeza)
     */
    void deleteByStatusAndCreatedAtBefore(AlertStatus status, Instant before);

    /**
     * Deleta todos os alertas de um item
     */
    void deleteByItemId(UUID itemId);

    /**
     * Verifica se existe alerta pendente para uma regra e item
     * (útil para evitar duplicação)
     */
    boolean existsByRuleIdAndItemIdAndStatus(UUID ruleId, UUID itemId, AlertStatus status);

    /**
     * Conta alertas pendentes de um usuário
     */
    long countByUserIdAndStatus(UUID userId, AlertStatus status);
}
