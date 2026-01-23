package br.com.harlemsilvas.itemcontrol.api.web.controller;

import br.com.harlemsilvas.itemcontrol.api.web.dto.request.CreateRuleRequest;
import br.com.harlemsilvas.itemcontrol.api.web.dto.request.UpdateRuleRequest;
import br.com.harlemsilvas.itemcontrol.api.web.dto.response.RuleResponse;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.rule.CreateRuleUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.rule.DeleteRuleUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.rule.GetRulesByItemUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.rule.UpdateRuleUseCase;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Rule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller REST para gerenciamento de Rules.
 */
@RestController
@RequestMapping("/api/v1/rules")
@RequiredArgsConstructor
@Tag(name = "Rules", description = "Gerenciamento de regras de alerta")
public class RuleController {

    private final CreateRuleUseCase createRuleUseCase;
    private final GetRulesByItemUseCase getRulesByItemUseCase;
    private final UpdateRuleUseCase updateRuleUseCase;
    private final DeleteRuleUseCase deleteRuleUseCase;

    @PostMapping
    @Operation(summary = "Criar regra", description = "Cria uma nova regra de alerta para um item")
    public ResponseEntity<RuleResponse> createRule(@Valid @RequestBody CreateRuleRequest request) {
        Rule rule = new Rule.Builder()
                .itemId(request.getItemId())
                .userId(request.getUserId())
                .name(request.getName())
                .ruleType(request.getRuleType())
                .condition(request.getCondition())
                .alertSettings(request.getAlertSettings())
                .enabled(request.getEnabled() != null ? request.getEnabled() : true)
                .build();

        try {
            Rule created = createRuleUseCase.execute(rule);
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
        } catch (CreateRuleUseCase.ItemNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Buscar regras por item", description = "Busca todas as regras de um item específico")
    public ResponseEntity<List<RuleResponse>> getRulesByItem(@RequestParam UUID itemId) {
        List<Rule> rules = getRulesByItemUseCase.execute(itemId);
        List<RuleResponse> response = rules.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar regra", description = "Atualiza uma regra existente")
    public ResponseEntity<RuleResponse> updateRule(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateRuleRequest request) {

        try {
            // Criar regra com dados atualizados (itemId e userId serão mantidos pelo UseCase)
            Rule updatedRule = new Rule.Builder()
                    .itemId(UUID.randomUUID()) // Será substituído pelo UseCase
                    .userId(UUID.randomUUID()) // Será substituído pelo UseCase
                    .name(request.getName())
                    .ruleType(request.getRuleType())
                    .condition(request.getCondition())
                    .alertSettings(request.getAlertSettings())
                    .enabled(request.getEnabled() != null ? request.getEnabled() : true)
                    .build();

            Rule updated = updateRuleUseCase.execute(id, updatedRule);
            return ResponseEntity.ok(toResponse(updated));
        } catch (UpdateRuleUseCase.RuleNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar regra", description = "Deleta uma regra existente")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        try {
            deleteRuleUseCase.execute(id);
            return ResponseEntity.noContent().build();
        } catch (DeleteRuleUseCase.RuleNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Converte Rule de domínio para RuleResponse.
     */
    private RuleResponse toResponse(Rule rule) {
        return RuleResponse.builder()
                .id(rule.getId())
                .itemId(rule.getItemId())
                .userId(rule.getUserId())
                .name(rule.getName())
                .ruleType(rule.getRuleType())
                .condition(rule.getCondition())
                .alertSettings(rule.getAlertSettings())
                .enabled(rule.isEnabled())
                .createdAt(rule.getCreatedAt())
                .updatedAt(rule.getUpdatedAt())
                .build();
    }
}
