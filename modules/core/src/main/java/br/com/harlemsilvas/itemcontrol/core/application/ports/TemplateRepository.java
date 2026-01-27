package br.com.harlemsilvas.itemcontrol.core.application.ports;

import br.com.harlemsilvas.itemcontrol.core.domain.model.Template;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Porta de persistência para Templates.
 */
public interface TemplateRepository {

    Template save(Template template);

    Optional<Template> findById(UUID id);

    Optional<Template> findByCodeGlobal(String code);

    Optional<Template> findByUserIdAndCode(UUID userId, String code);

    List<Template> findGlobal();

    List<Template> findByUserId(UUID userId);

    void deleteById(UUID id);

    boolean existsById(UUID id);
}
