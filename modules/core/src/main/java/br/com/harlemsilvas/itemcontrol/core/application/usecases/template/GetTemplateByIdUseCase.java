package br.com.harlemsilvas.itemcontrol.core.application.usecases.template;

import br.com.harlemsilvas.itemcontrol.core.application.ports.TemplateRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Template;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Use Case para buscar Template por ID.
 */
public class GetTemplateByIdUseCase {

    private final TemplateRepository templateRepository;

    public GetTemplateByIdUseCase(TemplateRepository templateRepository) {
        this.templateRepository = Objects.requireNonNull(templateRepository, "TemplateRepository cannot be null");
    }

    public Optional<Template> execute(UUID templateId) {
        if (templateId == null) {
            throw new IllegalArgumentException("TemplateId cannot be null");
        }
        return templateRepository.findById(templateId);
    }
}
