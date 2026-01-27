package br.com.harlemsilvas.itemcontrol.core.application.usecases.template;

import br.com.harlemsilvas.itemcontrol.core.application.ports.TemplateRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Template;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Use Case para listar templates.
 */
public class ListTemplatesUseCase {

    private final TemplateRepository templateRepository;

    public ListTemplatesUseCase(TemplateRepository templateRepository) {
        this.templateRepository = Objects.requireNonNull(templateRepository, "TemplateRepository cannot be null");
    }

    /**
     * Lista templates globais + os templates do usuário.
     */
    public List<Template> listAvailable(UUID userId) {
        List<Template> result = new ArrayList<>(templateRepository.findGlobal());
        if (userId != null) {
            result.addAll(templateRepository.findByUserId(userId));
        }
        return result;
    }

    public List<Template> listGlobal() {
        return templateRepository.findGlobal();
    }

    public List<Template> listByUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        return templateRepository.findByUserId(userId);
    }
}
