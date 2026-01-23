package br.com.harlemsilvas.itemcontrol.api.infra.mongo.adapter;

import br.com.harlemsilvas.itemcontrol.api.infra.mongo.document.RuleDocument;
import br.com.harlemsilvas.itemcontrol.api.infra.mongo.mapper.RuleDocumentMapper;
import br.com.harlemsilvas.itemcontrol.api.infra.mongo.repository.SpringDataRuleRepository;
import br.com.harlemsilvas.itemcontrol.core.application.ports.RuleRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Rule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter MongoDB para RuleRepository.
 */
@Repository
@RequiredArgsConstructor
public class MongoRuleRepositoryAdapter implements RuleRepository {

    private final SpringDataRuleRepository springDataRepository;
    private final RuleDocumentMapper mapper;

    @Override
    public Rule save(Rule rule) {
        RuleDocument document = mapper.toDocument(rule);
        RuleDocument saved = springDataRepository.save(document);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Rule> findById(UUID id) {
        return springDataRepository.findById(RuleDocument.toStringId(id))
                .map(mapper::toDomain);
    }

    @Override
    public List<Rule> findByItemId(UUID itemId) {
        return springDataRepository.findByItemId(RuleDocument.toStringId(itemId))
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rule> findByUserId(UUID userId) {
        return springDataRepository.findByUserId(RuleDocument.toStringId(userId))
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rule> findActiveByItemId(UUID itemId) {
        return springDataRepository.findByItemIdAndEnabled(
                        RuleDocument.toStringId(itemId), true)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        springDataRepository.deleteById(RuleDocument.toStringId(id));
    }

    @Override
    public void deleteByItemId(UUID itemId) {
        springDataRepository.deleteByItemId(RuleDocument.toStringId(itemId));
    }

    @Override
    public boolean existsById(UUID id) {
        return springDataRepository.existsById(RuleDocument.toStringId(id));
    }

    @Override
    public long countByItemId(UUID itemId) {
        return springDataRepository.findByItemId(RuleDocument.toStringId(itemId)).size();
    }
}
