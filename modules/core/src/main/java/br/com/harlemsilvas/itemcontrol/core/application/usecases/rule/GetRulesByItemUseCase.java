package br.com.harlemsilvas.itemcontrol.core.application.usecases.rule;

import br.com.harlemsilvas.itemcontrol.core.application.ports.RuleRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Rule;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Use Case para buscar regras de um item específico.
 */
public class GetRulesByItemUseCase {

    private final RuleRepository ruleRepository;

    public GetRulesByItemUseCase(RuleRepository ruleRepository) {
        this.ruleRepository = Objects.requireNonNull(ruleRepository, "RuleRepository cannot be null");
    }

    /**
     * Executa o caso de uso de busca de regras por item.
     *
     * @param itemId ID do item
     * @return Lista de regras do item
     * @throws IllegalArgumentException se itemId for nulo
     */
    public List<Rule> execute(UUID itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("ItemId cannot be null");
        }

        return ruleRepository.findByItemId(itemId);
    }
}
