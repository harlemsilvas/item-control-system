package br.com.harlemsilvas.itemcontrol.api.infra.mongo.adapter;

import br.com.harlemsilvas.itemcontrol.api.infra.mongo.document.EventDocument;
import br.com.harlemsilvas.itemcontrol.api.infra.mongo.mapper.EventDocumentMapper;
import br.com.harlemsilvas.itemcontrol.api.infra.mongo.repository.SpringDataEventRepository;
import br.com.harlemsilvas.itemcontrol.core.application.ports.EventRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.enums.EventType;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter que implementa EventRepository usando MongoDB.
 */
@Repository
@RequiredArgsConstructor
public class MongoEventRepositoryAdapter implements EventRepository {

    private final SpringDataEventRepository springDataRepository;
    private final EventDocumentMapper mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public Event save(Event event) {
        EventDocument document = mapper.toDocument(event);
        EventDocument saved = springDataRepository.save(document);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Event> findById(UUID id) {
        return springDataRepository.findById(EventDocument.toStringId(id))
            .map(mapper::toDomain);
    }

    @Override
    public List<Event> findByItemId(UUID itemId) {
        return springDataRepository.findByItemId(EventDocument.toStringId(itemId))
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByItemIdAndEventType(UUID itemId, EventType eventType) {
        return springDataRepository.findByItemIdAndEventType(EventDocument.toStringId(itemId), eventType)
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByItemIdAndEventDateBetween(UUID itemId, Instant startDate, Instant endDate) {
        return springDataRepository.findByItemIdAndEventDateBetween(
                EventDocument.toStringId(itemId), startDate, endDate)
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByUserId(UUID userId) {
        return springDataRepository.findByUserId(EventDocument.toStringId(userId))
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Event> findTopNByItemIdOrderByEventDateDesc(UUID itemId, int limit) {
        Query query = new Query()
            .addCriteria(Criteria.where("itemId").is(EventDocument.toStringId(itemId)))
            .with(Sort.by(Sort.Direction.DESC, "eventDate"))
            .limit(limit);

        return mongoTemplate.find(query, EventDocument.class)
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Event> findLatestByItemIdAndEventType(UUID itemId, EventType eventType) {
        return springDataRepository.findFirstByItemIdAndEventTypeOrderByEventDateDesc(
                EventDocument.toStringId(itemId), eventType)
            .map(mapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        springDataRepository.deleteById(EventDocument.toStringId(id));
    }

    @Override
    public void deleteByItemId(UUID itemId) {
        springDataRepository.deleteByItemId(EventDocument.toStringId(itemId));
    }

    @Override
    public long countByItemId(UUID itemId) {
        return springDataRepository.countByItemId(EventDocument.toStringId(itemId));
    }
}
