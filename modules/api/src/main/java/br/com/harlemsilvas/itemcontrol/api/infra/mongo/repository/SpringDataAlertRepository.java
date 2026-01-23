package br.com.harlemsilvas.itemcontrol.api.infra.mongo.repository;

import br.com.harlemsilvas.itemcontrol.api.infra.mongo.document.AlertDocument;
import br.com.harlemsilvas.itemcontrol.core.domain.enums.AlertStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data MongoDB Repository para AlertDocument.
 */
@Repository
public interface SpringDataAlertRepository extends MongoRepository<AlertDocument, String> {

    List<AlertDocument> findByItemId(String itemId);

    List<AlertDocument> findByUserId(String userId);

    List<AlertDocument> findByUserIdAndStatus(String userId, AlertStatus status);

    List<AlertDocument> findByDueAtBeforeAndStatus(Instant dueAt, AlertStatus status);

    List<AlertDocument> findByItemIdAndStatus(String itemId, AlertStatus status);

    long countByUserIdAndStatus(String userId, AlertStatus status);

    void deleteByItemId(String itemId);
}
