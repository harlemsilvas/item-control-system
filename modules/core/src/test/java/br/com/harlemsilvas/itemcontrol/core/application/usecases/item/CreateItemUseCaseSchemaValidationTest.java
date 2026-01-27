package br.com.harlemsilvas.itemcontrol.core.application.usecases.item;

import br.com.harlemsilvas.itemcontrol.core.application.ports.ItemRepository;
import br.com.harlemsilvas.itemcontrol.core.application.ports.TemplateRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Item;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Template;
import br.com.harlemsilvas.itemcontrol.core.domain.model.TemplateScope;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateItemUseCaseSchemaValidationTest {

    @Test
    void shouldReturn400StyleExceptionWhenMetadataDoesNotMatchSchema() {
        ItemRepository itemRepository = mock(ItemRepository.class);
        TemplateRepository templateRepository = mock(TemplateRepository.class);

        UUID userId = UUID.randomUUID();

        Template template = new Template.Builder()
            .scope(TemplateScope.USER)
            .userId(userId)
            .code("VEHICLE")
            .nameByLocale(Map.of("pt-BR", "Veículo"))
            .metadataSchema(Map.of(
                "year", Map.of("type", "number", "required", true)
            ))
            .build();

        when(templateRepository.findByUserIdAndCode(userId, "VEHICLE")).thenReturn(Optional.of(template));
        when(templateRepository.findByCodeGlobal("VEHICLE")).thenReturn(Optional.empty());
        when(itemRepository.save(any(Item.class))).thenAnswer(inv -> inv.getArgument(0));

        CreateItemUseCase useCase = new CreateItemUseCase(itemRepository, templateRepository);

        CreateItemUseCase.CreateItemCommand cmd = new CreateItemUseCase.CreateItemCommand();
        cmd.userId = userId;
        cmd.name = "Meu Carro";
        cmd.templateCode = "VEHICLE";
        cmd.metadata = Map.of("year", "2020"); // errado: string ao invés de number

        assertThatThrownBy(() -> useCase.execute(cmd))
            .isInstanceOf(br.com.harlemsilvas.itemcontrol.core.domain.service.MetadataSchemaValidator.MetadataSchemaValidationException.class);

        verify(itemRepository, never()).save(any(Item.class));
    }
}
