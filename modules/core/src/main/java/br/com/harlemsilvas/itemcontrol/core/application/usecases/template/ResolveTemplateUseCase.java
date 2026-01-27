package br.com.harlemsilvas.itemcontrol.core.application.usecases.template;

import br.com.harlemsilvas.itemcontrol.core.application.ports.TemplateRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Template;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Resolve um template para uso na criação do Item.
 *
 * Prioridade:
 * 1) se templateId informado -> busca por id
 * 2) se templateCode informado -> busca USER (userId+code) e depois GLOBAL (code)
 */
public class ResolveTemplateUseCase {

    private final TemplateRepository templateRepository;

    public ResolveTemplateUseCase(TemplateRepository templateRepository) {
        this.templateRepository = Objects.requireNonNull(templateRepository, "TemplateRepository cannot be null");
    }

    public Optional<Template> execute(UUID userId, UUID templateId, String templateCode) {
        if (templateId != null) {
            return templateRepository.findById(templateId);
        }

        if (templateCode == null || templateCode.trim().isEmpty()) {
            return Optional.empty();
        }

        if (userId != null) {
            Optional<Template> byUser = templateRepository.findByUserIdAndCode(userId, templateCode);
            if (byUser.isPresent()) {
                return byUser;
            }
        }

        return templateRepository.findByCodeGlobal(templateCode);
    }
}
