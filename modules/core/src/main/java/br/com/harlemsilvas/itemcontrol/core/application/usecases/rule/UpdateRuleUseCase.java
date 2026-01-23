package br.com.harlemsilvas.itemcontrol.core.application.usecases.rule;

import br.com.harlemsilvas.itemcontrol.core.application.ports.RuleRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Rule;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Use Case para atualizar uma regra existente.
 */
public class UpdateRuleUseCase {

    private final RuleRepository ruleRepository;

    public UpdateRuleUseCase(RuleRepository ruleRepository) {
        this.ruleRepository = Objects.requireNonNull(ruleRepository, "RuleRepository cannot be null");
    }

    /**
     * Executa o caso de uso de atualização de regra.
     *
     * @param id ID da regra a ser atualizada
     * @param updatedRule Regra com dados atualizados
     * @return Regra atualizada
     * @throws IllegalArgumentException se id ou updatedRule forem nulos
     * @throws RuleNotFoundException se a regra não for encontrada
     */
    public Rule execute(UUID id, Rule updatedRule) {
        if (id == null) {
            throw new IllegalArgumentException("Rule ID cannot be null");
        }
        if (updatedRule == null) {
            throw new IllegalArgumentException("Updated rule cannot be null");
        }

        // Verificar se a regra existe
        Optional<Rule> existingRule = ruleRepository.findById(id);
        if (existingRule.isEmpty()) {
            throw new RuleNotFoundException("Rule not found with id: " + id);
        }

        // Criar nova regra com ID mantido e dados atualizados
        Rule ruleToUpdate = new Rule.Builder()
                .id(id)
                .itemId(updatedRule.getItemId())
                .userId(updatedRule.getUserId())
                .name(updatedRule.getName())
                .ruleType(updatedRule.getRuleType())
                .condition(updatedRule.getCondition())
                .alertSettings(updatedRule.getAlertSettings())
                .enabled(updatedRule.isEnabled())
                .createdAt(existingRule.get().getCreatedAt())
                .build();

        return ruleRepository.save(ruleToUpdate);
    }

    /**
     * Exception lançada quando uma regra não é encontrada.
     */
    public static class RuleNotFoundException extends RuntimeException {
        public RuleNotFoundException(String message) {
            super(message);
        }
    }
}
