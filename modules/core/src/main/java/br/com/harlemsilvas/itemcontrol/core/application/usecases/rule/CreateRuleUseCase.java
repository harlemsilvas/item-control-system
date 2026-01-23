package br.com.harlemsilvas.itemcontrol.core.application.usecases.rule;

import br.com.harlemsilvas.itemcontrol.core.application.ports.ItemRepository;
import br.com.harlemsilvas.itemcontrol.core.application.ports.RuleRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Rule;

import java.util.Objects;

/**
 * Use Case para criar uma nova regra de alerta.
 */
public class CreateRuleUseCase {

    private final RuleRepository ruleRepository;
    private final ItemRepository itemRepository;

    public CreateRuleUseCase(RuleRepository ruleRepository, ItemRepository itemRepository) {
        this.ruleRepository = Objects.requireNonNull(ruleRepository, "RuleRepository cannot be null");
        this.itemRepository = Objects.requireNonNull(itemRepository, "ItemRepository cannot be null");
    }

    /**
     * Executa o caso de uso de criação de regra.
     *
     * @param rule Regra a ser criada
     * @return Regra criada
     * @throws IllegalArgumentException se a regra for nula
     * @throws ItemNotFoundException se o item associado não existir
     */
    public Rule execute(Rule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("Rule cannot be null");
        }

        // Verificar se o item existe
        if (!itemRepository.existsById(rule.getItemId())) {
            throw new ItemNotFoundException("Item not found with id: " + rule.getItemId());
        }

        return ruleRepository.save(rule);
    }

    /**
     * Exception lançada quando um item não é encontrado.
     */
    public static class ItemNotFoundException extends RuntimeException {
        public ItemNotFoundException(String message) {
            super(message);
        }
    }
}
