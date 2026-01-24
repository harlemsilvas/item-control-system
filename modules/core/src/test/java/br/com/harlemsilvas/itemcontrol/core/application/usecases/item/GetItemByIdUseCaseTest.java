package br.com.harlemsilvas.itemcontrol.core.application.usecases.item;

import br.com.harlemsilvas.itemcontrol.core.application.ports.ItemRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetItemByIdUseCase Tests")
class GetItemByIdUseCaseTest {

    @Mock
    private ItemRepository itemRepository;

    private GetItemByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetItemByIdUseCase(itemRepository);
    }

    @Test
    @DisplayName("Should get item by id successfully")
    void shouldGetItemByIdSuccessfully() {
        // Given
        UUID itemId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Item item = new Item.Builder()
                .id(itemId)
                .userId(userId)
                .name("Test Item")
                .nickname("test-001")
                .templateCode("GENERAL")
                .build();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // When
        Optional<Item> result = useCase.execute(itemId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(itemId);
        assertThat(result.get().getName()).isEqualTo("Test Item");

        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    @DisplayName("Should return empty when item not found")
    void shouldReturnEmptyWhenItemNotFound() {
        // Given
        UUID itemId = UUID.randomUUID();
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        // When
        Optional<Item> result = useCase.execute(itemId);

        // Then
        assertThat(result).isEmpty();
        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    @DisplayName("Should throw exception when id is null")
    void shouldThrowExceptionWhenIdIsNull() {
        // When & Then
        assertThatThrownBy(() -> useCase.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Item ID cannot be null");

        verify(itemRepository, never()).findById(any());
    }
}
