package br.com.harlemsilvas.itemcontrol.api.infra.mongo.mapper;

import br.com.harlemsilvas.itemcontrol.api.infra.mongo.document.RuleDocument;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Rule;
import org.springframework.stereotype.Component;

/**
 * Mapper para converter entre Rule (domain) e RuleDocument (MongoDB).
 */
@Component
public class RuleDocumentMapper {

    /**
     * Converte Rule de domínio para RuleDocument do MongoDB.
     */
    public RuleDocument toDocument(Rule rule) {
        if (rule == null) {
            return null;
        }

        return RuleDocument.builder()
                .id(RuleDocument.toStringId(rule.getId()))
                .itemId(RuleDocument.toStringId(rule.getItemId()))
                .userId(RuleDocument.toStringId(rule.getUserId()))
                .name(rule.getName())
                .ruleType(rule.getRuleType())
                .condition(rule.getCondition())
                .alertSettings(rule.getAlertSettings())
                .enabled(rule.isEnabled())
                .createdAt(rule.getCreatedAt())
                .updatedAt(rule.getUpdatedAt())
                .build();
    }

    /**
     * Converte RuleDocument do MongoDB para Rule de domínio.
     */
    public Rule toDomain(RuleDocument document) {
        if (document == null) {
            return null;
        }

        return new Rule.Builder()
                .id(RuleDocument.toUUID(document.getId()))
                .itemId(RuleDocument.toUUID(document.getItemId()))
                .userId(RuleDocument.toUUID(document.getUserId()))
                .name(document.getName())
                .ruleType(document.getRuleType())
                .condition(document.getCondition())
                .alertSettings(document.getAlertSettings())
                .enabled(document.isEnabled())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
}
