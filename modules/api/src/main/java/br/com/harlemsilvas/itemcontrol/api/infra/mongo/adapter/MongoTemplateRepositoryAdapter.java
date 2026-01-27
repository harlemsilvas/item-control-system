package br.com.harlemsilvas.itemcontrol.api.infra.mongo.adapter;

import br.com.harlemsilvas.itemcontrol.api.infra.mongo.document.TemplateDocument;
import br.com.harlemsilvas.itemcontrol.api.infra.mongo.mapper.TemplateDocumentMapper;
import br.com.harlemsilvas.itemcontrol.api.infra.mongo.repository.SpringDataTemplateRepository;
import br.com.harlemsilvas.itemcontrol.core.application.ports.TemplateRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Template;
import br.com.harlemsilvas.itemcontrol.core.domain.model.TemplateScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter que implementa TemplateRepository usando MongoDB.
 */
@Repository
@RequiredArgsConstructor
public class MongoTemplateRepositoryAdapter implements TemplateRepository {

    private final SpringDataTemplateRepository springDataRepository;
    private final TemplateDocumentMapper mapper;

    @Override
    public Template save(Template template) {
        TemplateDocument document = mapper.toDocument(template);
        TemplateDocument saved = springDataRepository.save(document);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Template> findById(UUID id) {
        return springDataRepository.findById(id.toString()).map(mapper::toDomain);
    }

    @Override
    public Optional<Template> findByCodeGlobal(String code) {
        return springDataRepository.findByScopeAndCode(TemplateScope.GLOBAL, code).map(mapper::toDomain);
    }

    @Override
    public Optional<Template> findByUserIdAndCode(UUID userId, String code) {
        return springDataRepository
            .findByUserIdAndScopeAndCode(userId.toString(), TemplateScope.USER, code)
            .map(mapper::toDomain);
    }

    @Override
    public List<Template> findGlobal() {
        return springDataRepository.findByScope(TemplateScope.GLOBAL)
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Template> findByUserId(UUID userId) {
        return springDataRepository.findByUserIdAndScope(userId.toString(), TemplateScope.USER)
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        springDataRepository.deleteById(id.toString());
    }

    @Override
    public boolean existsById(UUID id) {
        return springDataRepository.existsById(id.toString());
    }
}
