package br.com.harlemsilvas.itemcontrol.core.application.usecases.template;

import br.com.harlemsilvas.itemcontrol.core.application.ports.TemplateRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Template;
import br.com.harlemsilvas.itemcontrol.core.domain.model.TemplateScope;

import java.util.Objects;

/**
 * Use Case para criar um Template.
 */
public class CreateTemplateUseCase {

    private final TemplateRepository templateRepository;

    public CreateTemplateUseCase(TemplateRepository templateRepository) {
        this.templateRepository = Objects.requireNonNull(templateRepository, "TemplateRepository cannot be null");
    }

    public Template execute(Template template) {
        if (template == null) {
            throw new IllegalArgumentException("Template cannot be null");
        }

        // valida unicidade do code no escopo
        if (template.getScope() == TemplateScope.GLOBAL) {
            templateRepository.findByCodeGlobal(template.getCode())
                .ifPresent(existing -> {
                    throw new TemplateCodeAlreadyExistsException(template.getCode());
                });
        } else {
            templateRepository.findByUserIdAndCode(template.getUserId(), template.getCode())
                .ifPresent(existing -> {
                    throw new TemplateCodeAlreadyExistsException(template.getCode());
                });
        }

        return templateRepository.save(template);
    }

    public static class TemplateCodeAlreadyExistsException extends RuntimeException {
        public TemplateCodeAlreadyExistsException(String code) {
            super("Template code already exists: " + code);
        }
    }
}
