package br.com.harlemsilvas.itemcontrol.core.application.ports;

import br.com.harlemsilvas.itemcontrol.core.domain.model.Rule;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port (interface) para persistência de Rules.
 */
public interface RuleRepository {

    /**
     * Salva uma regra
     */
    Rule save(Rule rule);

    /**
     * Busca uma regra por ID
     */
    Optional<Rule> findById(UUID id);

    /**
     * Busca todas as regras de um item
     */
    List<Rule> findByItemId(UUID itemId);

    /**
     * Busca regras habilitadas de um item
     */
    List<Rule> findByItemIdAndEnabled(UUID itemId, boolean enabled);

    /**
     * Busca todas as regras habilitadas (para processamento)
     */
    List<Rule> findAllEnabled();

    /**
     * Busca regras por usuário
     */
    List<Rule> findByUserId(UUID userId);

    /**
     * Deleta uma regra por ID
     */
    void deleteById(UUID id);

    /**
     * Deleta todas as regras de um item
     */
    void deleteByItemId(UUID itemId);

    /**
     * Verifica se uma regra existe
     */
    boolean existsById(UUID id);

    /**
     * Conta regras de um item
     */
    long countByItemId(UUID itemId);

    /**
     * Conta regras habilitadas
     */
    long countByEnabled(boolean enabled);
}
