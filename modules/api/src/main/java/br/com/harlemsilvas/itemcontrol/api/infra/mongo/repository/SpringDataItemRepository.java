package br.com.harlemsilvas.itemcontrol.api.infra.mongo.repository;

import br.com.harlemsilvas.itemcontrol.api.infra.mongo.document.ItemDocument;
import br.com.harlemsilvas.itemcontrol.core.domain.enums.ItemStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Spring Data MongoDB Repository para ItemDocument.
 */
@Repository
public interface SpringDataItemRepository extends MongoRepository<ItemDocument, String> {

    List<ItemDocument> findByUserId(String userId);

    List<ItemDocument> findByUserIdAndStatus(String userId, ItemStatus status);

    List<ItemDocument> findByUserIdAndTemplateCode(String userId, String templateCode);

    List<ItemDocument> findByUserIdAndTagsIn(String userId, Set<String> tags);

    List<ItemDocument> findByCategoryId(String categoryId);

    long countByUserId(String userId);
}
