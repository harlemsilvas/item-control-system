package br.com.harlemsilvas.itemcontrol.core.application.usecases.item;

import br.com.harlemsilvas.itemcontrol.core.application.ports.ItemRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateItemUseCase Tests")
class CreateItemUseCaseTest {

    @Mock
    private ItemRepository itemRepository;

    private CreateItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateItemUseCase(itemRepository);
    }

    @Test
    @DisplayName("Should create item successfully")
    void shouldCreateItemSuccessfully() {
        // Given
        UUID userId = UUID.randomUUID();
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("brand", "Toyota");
        metadata.put("model", "Corolla");

        Item item = new Item.Builder()
                .userId(userId)
                .name("Meu Carro")
                .nickname("corolla-2020")
                .templateCode("VEHICLE")
                .metadata(metadata)
                .build();

        when(itemRepository.save(any(Item.class))).thenReturn(item);

        // When
        Item result = useCase.execute(item);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Meu Carro");
        assertThat(result.getNickname()).isEqualTo("corolla-2020");
        assertThat(result.getUserId()).isEqualTo(userId);

        verify(itemRepository, times(1)).save(item);
    }

    @Test
    @DisplayName("Should throw exception when item is null")
    void shouldThrowExceptionWhenItemIsNull() {
        // When & Then
        assertThatThrownBy(() -> useCase.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Item cannot be null");

        verify(itemRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should create item with minimal information")
    void shouldCreateItemWithMinimalInformation() {
        // Given
        UUID userId = UUID.randomUUID();
        Item item = new Item.Builder()
                .userId(userId)
                .name("Simple Item")
                .nickname("simple-001")
                .templateCode("GENERAL")
                .build();

        when(itemRepository.save(any(Item.class))).thenReturn(item);

        // When
        Item result = useCase.execute(item);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Simple Item");
        assertThat(result.isActive()).isTrue();

        verify(itemRepository, times(1)).save(item);
    }
}
