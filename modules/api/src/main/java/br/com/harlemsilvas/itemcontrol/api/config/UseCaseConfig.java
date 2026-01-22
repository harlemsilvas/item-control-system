package br.com.harlemsilvas.itemcontrol.api.config;

import br.com.harlemsilvas.itemcontrol.core.application.ports.EventRepository;
import br.com.harlemsilvas.itemcontrol.core.application.ports.ItemRepository;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.event.GetEventHistoryUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.event.RegisterEventUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.item.CreateItemUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.item.GetItemByIdUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.item.ListUserItemsUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.item.UpdateItemMetadataUseCase;
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
}
