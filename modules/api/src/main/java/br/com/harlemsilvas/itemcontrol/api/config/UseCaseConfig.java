package br.com.harlemsilvas.itemcontrol.api.config;

import br.com.harlemsilvas.itemcontrol.core.application.ports.AlertRepository;
import br.com.harlemsilvas.itemcontrol.core.application.ports.EventRepository;
import br.com.harlemsilvas.itemcontrol.core.application.ports.ItemRepository;
import br.com.harlemsilvas.itemcontrol.core.application.ports.RuleRepository;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.alert.AcknowledgeAlertUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.alert.CreateAlertUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.alert.ListPendingAlertsUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.alert.ResolveAlertUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.event.GetEventHistoryUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.event.RegisterEventUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.item.CreateItemUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.item.GetItemByIdUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.item.ListUserItemsUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.item.UpdateItemMetadataUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.rule.CreateRuleUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.rule.DeleteRuleUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.rule.GetRulesByItemUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.rule.UpdateRuleUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração para injeção de dependências dos Use Cases.
 */
@Configuration
public class UseCaseConfig {

    // Item Use Cases

    @Bean
    public CreateItemUseCase createItemUseCase(ItemRepository itemRepository) {
        return new CreateItemUseCase(itemRepository);
    }

    @Bean
    public GetItemByIdUseCase getItemByIdUseCase(ItemRepository itemRepository) {
        return new GetItemByIdUseCase(itemRepository);
    }

    @Bean
    public ListUserItemsUseCase listUserItemsUseCase(ItemRepository itemRepository) {
        return new ListUserItemsUseCase(itemRepository);
    }

    @Bean
    public UpdateItemMetadataUseCase updateItemMetadataUseCase(ItemRepository itemRepository) {
        return new UpdateItemMetadataUseCase(itemRepository);
    }

    // Event Use Cases

    @Bean
    public RegisterEventUseCase registerEventUseCase(
            EventRepository eventRepository,
            ItemRepository itemRepository) {
        return new RegisterEventUseCase(eventRepository, itemRepository);
    }

    @Bean
    public GetEventHistoryUseCase getEventHistoryUseCase(EventRepository eventRepository) {
        return new GetEventHistoryUseCase(eventRepository);
    }

    // Alert Use Cases

    @Bean
    public CreateAlertUseCase createAlertUseCase(
            AlertRepository alertRepository,
            ItemRepository itemRepository) {
        return new CreateAlertUseCase(alertRepository, itemRepository);
    }

    @Bean
    public ListPendingAlertsUseCase listPendingAlertsUseCase(AlertRepository alertRepository) {
        return new ListPendingAlertsUseCase(alertRepository);
    }

    @Bean
    public AcknowledgeAlertUseCase acknowledgeAlertUseCase(AlertRepository alertRepository) {
        return new AcknowledgeAlertUseCase(alertRepository);
    }

    @Bean
    public ResolveAlertUseCase resolveAlertUseCase(AlertRepository alertRepository) {
        return new ResolveAlertUseCase(alertRepository);
    }

    // Rule Use Cases

    @Bean
    public CreateRuleUseCase createRuleUseCase(
            RuleRepository ruleRepository,
            ItemRepository itemRepository) {
        return new CreateRuleUseCase(ruleRepository, itemRepository);
    }

    @Bean
    public GetRulesByItemUseCase getRulesByItemUseCase(RuleRepository ruleRepository) {
        return new GetRulesByItemUseCase(ruleRepository);
    }

    @Bean
    public UpdateRuleUseCase updateRuleUseCase(RuleRepository ruleRepository) {
        return new UpdateRuleUseCase(ruleRepository);
    }

    @Bean
    public DeleteRuleUseCase deleteRuleUseCase(RuleRepository ruleRepository) {
        return new DeleteRuleUseCase(ruleRepository);
    }
}
