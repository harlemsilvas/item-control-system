package br.com.harlemsilvas.itemcontrol.core.domain.service;

import br.com.harlemsilvas.itemcontrol.core.domain.model.Template;
import br.com.harlemsilvas.itemcontrol.core.domain.model.TemplateScope;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MetadataSchemaValidatorTest {

    @Test
    void shouldFailWhenRequiredFieldIsMissing() {
        Template template = new Template.Builder()
            .scope(TemplateScope.USER)
            .userId(UUID.randomUUID())
            .code("TEST")
            .nameByLocale(Map.of("pt-BR", "Teste"))
            .metadataSchema(Map.of(
                "brand", Map.of("type", "string", "required", true)
            ))
            .build();

        assertThatThrownBy(() -> MetadataSchemaValidator.validateOrThrow(template, Map.of()))
            .isInstanceOf(MetadataSchemaValidator.MetadataSchemaValidationException.class)
            .hasMessageContaining("Missing required metadata field: brand");
    }

    @Test
    void shouldFailWhenTypeDoesNotMatch() {
        Template template = new Template.Builder()
            .scope(TemplateScope.USER)
            .userId(UUID.randomUUID())
            .code("TEST")
            .nameByLocale(Map.of("pt-BR", "Teste"))
            .metadataSchema(Map.of(
                "year", Map.of("type", "number", "required", true)
            ))
            .build();

        assertThatThrownBy(() -> MetadataSchemaValidator.validateOrThrow(template, Map.of("year", "2020")))
            .isInstanceOf(MetadataSchemaValidator.MetadataSchemaValidationException.class)
            .hasMessageContaining("Invalid metadata type for field 'year'");
    }
}
