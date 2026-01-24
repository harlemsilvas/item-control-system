package br.com.harlemsilvas.itemcontrol.core.application.usecases.event;

import br.com.harlemsilvas.itemcontrol.core.application.ports.EventRepository;
import br.com.harlemsilvas.itemcontrol.core.application.ports.ItemRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Event;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterEventUseCase Tests")
class RegisterEventUseCaseTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ItemRepository itemRepository;

    private RegisterEventUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegisterEventUseCase(eventRepository, itemRepository);
    }

    @Test
    @DisplayName("Should register event successfully")
    void shouldRegisterEventSuccessfully() {
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

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("value", 100.0);

        Event event = new Event.Builder()
                .itemId(itemId)
                .eventType("MAINTENANCE")
                .description("Test event")
                .metrics(metrics)
                .occurredAt(Instant.now())
                .build();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // When
        Event result = useCase.execute(event);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getItemId()).isEqualTo(itemId);
        assertThat(result.getEventType()).isEqualTo("MAINTENANCE");

        verify(itemRepository, times(1)).findById(itemId);
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    @DisplayName("Should throw exception when event is null")
    void shouldThrowExceptionWhenEventIsNull() {
        // When & Then
        assertThatThrownBy(() -> useCase.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Event cannot be null");

        verify(eventRepository, never()).save(any());
        verify(itemRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should throw exception when item not found")
    void shouldThrowExceptionWhenItemNotFound() {
        // Given
        UUID itemId = UUID.randomUUID();

        Event event = new Event.Builder()
                .itemId(itemId)
                .eventType("MAINTENANCE")
                .description("Test event")
                .occurredAt(Instant.now())
                .build();

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> useCase.execute(event))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Item not found");

        verify(itemRepository, times(1)).findById(itemId);
        verify(eventRepository, never()).save(any());
    }
}

