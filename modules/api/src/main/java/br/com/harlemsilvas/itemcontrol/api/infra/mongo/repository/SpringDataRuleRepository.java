package br.com.harlemsilvas.itemcontrol.api.infra.mongo.repository;

import br.com.harlemsilvas.itemcontrol.api.infra.mongo.document.RuleDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository para RuleDocument.
 */
@Repository
public interface SpringDataRuleRepository extends MongoRepository<RuleDocument, String> {

    /**
     * Busca regras por itemId.
     */
    List<RuleDocument> findByItemId(String itemId);

    /**
     * Busca regras por userId.
     */
    List<RuleDocument> findByUserId(String userId);

    /**
     * Busca regras ativas por itemId.
     */
    List<RuleDocument> findByItemIdAndEnabled(String itemId, boolean enabled);

    /**
     * Busca todas as regras por status enabled.
     */
    List<RuleDocument> findByEnabled(boolean enabled);

    /**
     * Deleta todas as regras de um item.
     */
    void deleteByItemId(String itemId);

    /**
     * Conta regras por status enabled/disabled.
     */
    long countByEnabled(boolean enabled);
}
