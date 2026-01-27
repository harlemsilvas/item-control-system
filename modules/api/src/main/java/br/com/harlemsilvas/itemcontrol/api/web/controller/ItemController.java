package br.com.harlemsilvas.itemcontrol.api.web.controller;

import br.com.harlemsilvas.itemcontrol.api.web.dto.request.CreateItemRequest;
import br.com.harlemsilvas.itemcontrol.api.web.dto.response.ItemResponse;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.item.CreateItemUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.item.GetItemByIdUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.item.ListUserItemsUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.item.UpdateItemMetadataUseCase;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Item;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller REST para gerenciamento de Items.
 */
@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
@Tag(name = "Items", description = "API para gerenciamento de itens")
public class ItemController {

    private final CreateItemUseCase createItemUseCase;
    private final GetItemByIdUseCase getItemByIdUseCase;
    private final ListUserItemsUseCase listUserItemsUseCase;
    private final UpdateItemMetadataUseCase updateItemMetadataUseCase;

    @PostMapping
    @Operation(summary = "Criar novo item", description = "Cria um novo item no sistema")
    public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody CreateItemRequest request) {

        CreateItemUseCase.CreateItemCommand command = new CreateItemUseCase.CreateItemCommand();
        command.userId = request.getUserId();
        command.name = request.getName();
        command.nickname = request.getNickname();
        command.categoryId = request.getCategoryId();
        command.templateId = request.getTemplateId();
        command.templateCode = request.getTemplateCode();
        command.tags = request.getTags();
        command.metadata = request.getMetadata();

        if (request.getNewTemplate() != null) {
            CreateItemUseCase.CreateItemCommand.TemplateCreateCommand t = new CreateItemUseCase.CreateItemCommand.TemplateCreateCommand();
            t.scope = request.getNewTemplate().getScope();
            t.code = request.getNewTemplate().getCode();
            t.nameByLocale = request.getNewTemplate().getNameByLocale();
            t.descriptionByLocale = request.getNewTemplate().getDescriptionByLocale();
            t.metadataSchema = request.getNewTemplate().getMetadataSchema();
            command.newTemplate = t;
        }

        Item created = createItemUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item por ID", description = "Retorna um item específico pelo ID")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable UUID id) {
        return getItemByIdUseCase.execute(id)
            .map(item -> ResponseEntity.ok(toResponse(item)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar itens do usuário", description = "Lista todos os itens de um usuário")
    public ResponseEntity<List<ItemResponse>> listUserItems(@RequestParam UUID userId) {
        List<Item> items = listUserItemsUseCase.execute(userId);
        List<ItemResponse> response = items.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    @Operation(summary = "Listar itens ativos", description = "Lista apenas itens ativos do usuário")
    public ResponseEntity<List<ItemResponse>> listActiveItems(@RequestParam UUID userId) {
        List<Item> items = listUserItemsUseCase.executeActiveOnly(userId);
        List<ItemResponse> response = items.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/metadata")
    @Operation(summary = "Atualizar metadata", description = "Atualiza os metadados de um item")
    public ResponseEntity<ItemResponse> updateMetadata(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> metadata) {
        try {
            Item updated = updateItemMetadataUseCase.execute(id, metadata);
            return ResponseEntity.ok(toResponse(updated));
        } catch (UpdateItemMetadataUseCase.ItemNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Converte Item para ItemResponse.
     */
    private ItemResponse toResponse(Item item) {
        return ItemResponse.builder()
            .id(item.getId())
            .userId(item.getUserId())
            .name(item.getName())
            .nickname(item.getNickname())
            .categoryId(item.getCategoryId())
            .templateCode(item.getTemplateCode())
            .status(item.getStatus())
            .tags(item.getTags())
            .metadata(item.getMetadata())
            .createdAt(item.getCreatedAt())
            .updatedAt(item.getUpdatedAt())
            .build();
    }
}
