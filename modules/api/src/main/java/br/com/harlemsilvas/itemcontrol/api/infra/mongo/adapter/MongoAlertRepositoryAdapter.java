package br.com.harlemsilvas.itemcontrol.api.infra.mongo.adapter;

import br.com.harlemsilvas.itemcontrol.api.infra.mongo.document.AlertDocument;
import br.com.harlemsilvas.itemcontrol.api.infra.mongo.mapper.AlertDocumentMapper;
import br.com.harlemsilvas.itemcontrol.api.infra.mongo.repository.SpringDataAlertRepository;
import br.com.harlemsilvas.itemcontrol.core.application.ports.AlertRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.enums.AlertStatus;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Alert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter que implementa AlertRepository usando MongoDB.
 */
@Repository
@RequiredArgsConstructor
public class MongoAlertRepositoryAdapter implements AlertRepository {

    private final SpringDataAlertRepository springDataRepository;
    private final AlertDocumentMapper mapper;

    @Override
    public Alert save(Alert alert) {
        AlertDocument document = mapper.toDocument(alert);
        AlertDocument saved = springDataRepository.save(document);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Alert> findById(UUID id) {
        return springDataRepository.findById(AlertDocument.toStringId(id))
                .map(mapper::toDomain);
    }

    @Override
    public List<Alert> findByItemId(UUID itemId) {
        return springDataRepository.findByItemId(AlertDocument.toStringId(itemId))
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Alert> findByUserId(UUID userId) {
        return springDataRepository.findByUserId(AlertDocument.toStringId(userId))
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Alert> findByUserIdAndStatus(UUID userId, AlertStatus status) {
        return springDataRepository.findByUserIdAndStatus(
                        AlertDocument.toStringId(userId), status)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        springDataRepository.deleteById(AlertDocument.toStringId(id));
    }

    @Override
    public void deleteByItemId(UUID itemId) {
        springDataRepository.deleteByItemId(AlertDocument.toStringId(itemId));
    }

    @Override
    public long countByUserIdAndStatus(UUID userId, AlertStatus status) {
        return springDataRepository.countByUserIdAndStatus(
                AlertDocument.toStringId(userId), status);
    }
}
