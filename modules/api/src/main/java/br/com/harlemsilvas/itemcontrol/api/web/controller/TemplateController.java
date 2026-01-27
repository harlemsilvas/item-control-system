package br.com.harlemsilvas.itemcontrol.api.web.controller;

import br.com.harlemsilvas.itemcontrol.api.web.dto.request.CreateTemplateRequest;
import br.com.harlemsilvas.itemcontrol.api.web.dto.request.UpdateTemplateRequest;
import br.com.harlemsilvas.itemcontrol.api.web.dto.response.TemplateResponse;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.template.CreateTemplateUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.template.DeleteTemplateUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.template.GetTemplateByIdUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.template.ListTemplatesUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.template.UpdateTemplateUseCase;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Template;
import br.com.harlemsilvas.itemcontrol.core.domain.model.TemplateScope;
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
 * Controller REST para gerenciamento de Templates.
 */
@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
@Tag(name = "Templates", description = "API para gerenciamento de templates")
public class TemplateController {

    private final CreateTemplateUseCase createTemplateUseCase;
    private final UpdateTemplateUseCase updateTemplateUseCase;
    private final DeleteTemplateUseCase deleteTemplateUseCase;
    private final GetTemplateByIdUseCase getTemplateByIdUseCase;
    private final ListTemplatesUseCase listTemplatesUseCase;

    @PostMapping
    @Operation(summary = "Criar template", description = "Cria um novo template (GLOBAL ou USER)")
    public ResponseEntity<TemplateResponse> create(@Valid @RequestBody CreateTemplateRequest request,
                                                  @RequestHeader(name = "Accept-Language", required = false) String locale) {

        Template template = new Template.Builder()
            .userId(request.getScope() == TemplateScope.USER ? request.getUserId() : null)
            .scope(request.getScope())
            .code(request.getCode())
            .nameByLocale(request.getNameByLocale())
            .descriptionByLocale(request.getDescriptionByLocale())
            .metadataSchema(request.getMetadataSchema())
            .build();

        Template created = createTemplateUseCase.execute(template);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created, locale));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar template por ID")
    public ResponseEntity<TemplateResponse> getById(@PathVariable UUID id,
                                                    @RequestHeader(name = "Accept-Language", required = false) String locale) {

        return getTemplateByIdUseCase.execute(id)
            .map(t -> ResponseEntity.ok(toResponse(t, locale)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar templates disponíveis", description = "Lista GLOBAL + USER (se userId informado)")
    public ResponseEntity<List<TemplateResponse>> list(@RequestParam(required = false) UUID userId,
                                                       @RequestHeader(name = "Accept-Language", required = false) String locale) {

        List<Template> templates = listTemplatesUseCase.listAvailable(userId);
        return ResponseEntity.ok(templates.stream().map(t -> toResponse(t, locale)).collect(Collectors.toList()));
    }

    @GetMapping("/global")
    @Operation(summary = "Listar templates globais")
    public ResponseEntity<List<TemplateResponse>> listGlobal(
        @RequestHeader(name = "Accept-Language", required = false) String locale) {

        List<Template> templates = listTemplatesUseCase.listGlobal();
        return ResponseEntity.ok(templates.stream().map(t -> toResponse(t, locale)).collect(Collectors.toList()));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Listar templates do usuário")
    public ResponseEntity<List<TemplateResponse>> listUser(@PathVariable UUID userId,
                                                           @RequestHeader(name = "Accept-Language", required = false) String locale) {

        List<Template> templates = listTemplatesUseCase.listByUser(userId);
        return ResponseEntity.ok(templates.stream().map(t -> toResponse(t, locale)).collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar template")
    public ResponseEntity<TemplateResponse> update(@PathVariable UUID id,
                                                   @Valid @RequestBody UpdateTemplateRequest request,
                                                   @RequestHeader(name = "Accept-Language", required = false) String locale) {

        Template updatedData = new Template.Builder()
            .scope(TemplateScope.GLOBAL) // será ignorado no merge
            .code("IGNORED") // será ignorado no merge
            .nameByLocale(request.getNameByLocale())
            .descriptionByLocale(request.getDescriptionByLocale())
            .metadataSchema(request.getMetadataSchema())
            .build();

        try {
            Template updated = updateTemplateUseCase.execute(id, updatedData);
            return ResponseEntity.ok(toResponse(updated, locale));
        } catch (UpdateTemplateUseCase.TemplateNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar template")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        try {
            deleteTemplateUseCase.execute(id);
            return ResponseEntity.noContent().build();
        } catch (DeleteTemplateUseCase.TemplateNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private TemplateResponse toResponse(Template template, String locale) {
        return TemplateResponse.builder()
            .id(template.getId())
            .userId(template.getUserId())
            .scope(template.getScope())
            .code(template.getCode())
            .nameByLocale(template.getNameByLocale())
            .descriptionByLocale(template.getDescriptionByLocale())
            .metadataSchema(template.getMetadataSchema())
            .name(template.resolveName(locale))
            .description(template.resolveDescription(locale))
            .createdAt(template.getCreatedAt())
            .updatedAt(template.getUpdatedAt())
            .build();
    }
}
