package br.com.harlemsilvas.itemcontrol.api.infra.mongo.repository;

import br.com.harlemsilvas.itemcontrol.api.infra.mongo.document.TemplateDocument;
import br.com.harlemsilvas.itemcontrol.core.domain.model.TemplateScope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB Repository para TemplateDocument.
 */
@Repository
public interface SpringDataTemplateRepository extends MongoRepository<TemplateDocument, String> {

    Optional<TemplateDocument> findByScopeAndCode(TemplateScope scope, String code);

    Optional<TemplateDocument> findByUserIdAndScopeAndCode(String userId, TemplateScope scope, String code);

    List<TemplateDocument> findByScope(TemplateScope scope);

    List<TemplateDocument> findByUserIdAndScope(String userId, TemplateScope scope);
}
