package br.com.harlemsilvas.itemcontrol.core.application.usecases.template;

import br.com.harlemsilvas.itemcontrol.core.application.ports.TemplateRepository;

import java.util.Objects;
import java.util.UUID;

/**
 * Use Case para deletar um Template.
 */
public class DeleteTemplateUseCase {

    private final TemplateRepository templateRepository;

    public DeleteTemplateUseCase(TemplateRepository templateRepository) {
        this.templateRepository = Objects.requireNonNull(templateRepository, "TemplateRepository cannot be null");
    }

    public void execute(UUID templateId) {
        if (templateId == null) {
            throw new IllegalArgumentException("TemplateId cannot be null");
        }
        if (!templateRepository.existsById(templateId)) {
            throw new TemplateNotFoundException(templateId);
        }
        templateRepository.deleteById(templateId);
    }

    public static class TemplateNotFoundException extends RuntimeException {
        public TemplateNotFoundException(UUID id) {
            super("Template not found: " + id);
        }
    }
}
