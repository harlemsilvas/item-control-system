package br.com.harlemsilvas.itemcontrol.api.infra.mongo.document;

import br.com.harlemsilvas.itemcontrol.core.domain.model.TemplateScope;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

/**
 * Documento MongoDB para Template.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "templates")
@CompoundIndexes({
    // code único para GLOBAL (userId null) e para USER (userId preenchido). O Mongo considera null como valor, então isso funciona.
    @CompoundIndex(name = "uk_templates_user_scope_code", def = "{'userId': 1, 'scope': 1, 'code': 1}", unique = true)
})
public class TemplateDocument {

    @Id
    private String id;

    /** userId pode ser null para GLOBAL */
    private String userId;

    @Indexed(direction = IndexDirection.ASCENDING)
    private TemplateScope scope;

    @Indexed(direction = IndexDirection.ASCENDING)
    private String code;

    private Map<String, String> nameByLocale;

    private Map<String, String> descriptionByLocale;

    private Map<String, Object> metadataSchema;

    private Instant createdAt;

    private Instant updatedAt;
}
