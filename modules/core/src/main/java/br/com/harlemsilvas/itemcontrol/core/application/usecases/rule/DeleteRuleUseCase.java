package br.com.harlemsilvas.itemcontrol.core.application.usecases.rule;

import br.com.harlemsilvas.itemcontrol.core.application.ports.RuleRepository;

import java.util.Objects;
import java.util.UUID;

/**
 * Use Case para deletar uma regra.
 */
public class DeleteRuleUseCase {

    private final RuleRepository ruleRepository;

    public DeleteRuleUseCase(RuleRepository ruleRepository) {
        this.ruleRepository = Objects.requireNonNull(ruleRepository, "RuleRepository cannot be null");
    }

    /**
     * Executa o caso de uso de deleção de regra.
     *
     * @param id ID da regra a ser deletada
     * @throws IllegalArgumentException se id for nulo
     * @throws RuleNotFoundException se a regra não for encontrada
     */
    public void execute(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Rule ID cannot be null");
        }

        // Verificar se a regra existe
        if (!ruleRepository.existsById(id)) {
            throw new RuleNotFoundException("Rule not found with id: " + id);
        }

        ruleRepository.deleteById(id);
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
