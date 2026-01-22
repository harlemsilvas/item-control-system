package br.com.harlemsilvas.itemcontrol.api.infra.mongo.repository;

import br.com.harlemsilvas.itemcontrol.api.infra.mongo.document.EventDocument;
import br.com.harlemsilvas.itemcontrol.core.domain.enums.EventType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB Repository para EventDocument.
 */
@Repository
public interface SpringDataEventRepository extends MongoRepository<EventDocument, String> {

    List<EventDocument> findByItemId(String itemId);

    List<EventDocument> findByItemIdAndEventType(String itemId, EventType eventType);

    List<EventDocument> findByItemIdAndEventDateBetween(String itemId, Instant startDate, Instant endDate);

    List<EventDocument> findByUserId(String userId);

    @Query("{ 'itemId': ?0 }")
    List<EventDocument> findTopNByItemIdOrderByEventDateDesc(String itemId, int limit);

    @Query("{ 'itemId': ?0, 'eventType': ?1 }")
    Optional<EventDocument> findFirstByItemIdAndEventTypeOrderByEventDateDesc(String itemId, EventType eventType);

    void deleteByItemId(String itemId);

    long countByItemId(String itemId);
}
