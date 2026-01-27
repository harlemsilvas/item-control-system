package br.com.harlemsilvas.itemcontrol.api.web.dto.request;

import br.com.harlemsilvas.itemcontrol.core.domain.model.TemplateScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Quando o usuário escolhe "novo template" durante a criação do Item.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemTemplateRequest {

    private TemplateScope scope;

    @NotBlank(message = "Code cannot be blank")
    private String code;

    @NotEmpty(message = "nameByLocale cannot be empty")
    private Map<String, String> nameByLocale;

    private Map<String, String> descriptionByLocale;

    private Map<String, Object> metadataSchema;
}
