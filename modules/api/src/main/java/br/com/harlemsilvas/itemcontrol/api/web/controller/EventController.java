package br.com.harlemsilvas.itemcontrol.api.web.controller;

import br.com.harlemsilvas.itemcontrol.api.web.dto.request.RegisterEventRequest;
import br.com.harlemsilvas.itemcontrol.api.web.dto.response.EventResponse;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.event.GetEventHistoryUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.event.RegisterEventUseCase;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Event;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller REST para gerenciamento de Events.
 */
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "API para registro e consulta de eventos")
public class EventController {

    private final RegisterEventUseCase registerEventUseCase;
    private final GetEventHistoryUseCase getEventHistoryUseCase;

    @PostMapping
    @Operation(summary = "Registrar evento", description = "Registra um novo evento para um item")
    public ResponseEntity<EventResponse> registerEvent(@Valid @RequestBody RegisterEventRequest request) {
        Event event = new Event.Builder()
            .itemId(request.getItemId())
            .userId(request.getUserId())
            .eventType(request.getEventType())
            .eventDate(request.getEventDate())
            .description(request.getDescription())
            .metrics(request.getMetrics())
            .build();

        try {
            Event registered = registerEventUseCase.execute(event);
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(registered));
        } catch (RegisterEventUseCase.ItemNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Listar eventos de um item", description = "Retorna histórico de eventos de um item")
    public ResponseEntity<List<EventResponse>> getEventHistory(@RequestParam UUID itemId) {
        List<Event> events = getEventHistoryUseCase.execute(itemId);
        List<EventResponse> response = events.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent")
    @Operation(summary = "Últimos eventos", description = "Retorna os N últimos eventos de um item")
    public ResponseEntity<List<EventResponse>> getRecentEvents(
            @RequestParam UUID itemId,
            @RequestParam(defaultValue = "10") int limit) {
        List<Event> events = getEventHistoryUseCase.executeWithLimit(itemId, limit);
        List<EventResponse> response = events.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Converte Event para EventResponse.
     */
    private EventResponse toResponse(Event event) {
        return EventResponse.builder()
            .id(event.getId())
            .itemId(event.getItemId())
            .userId(event.getUserId())
            .eventType(event.getEventType())
            .eventDate(event.getEventDate())
            .description(event.getDescription())
            .metrics(event.getMetrics())
            .createdAt(event.getCreatedAt())
            .build();
    }
}
