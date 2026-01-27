package br.com.harlemsilvas.itemcontrol.api.web.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO para atualização de Template.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTemplateRequest {

    @NotEmpty(message = "nameByLocale cannot be empty")
    private Map<String, String> nameByLocale;

    private Map<String, String> descriptionByLocale;

    private Map<String, Object> metadataSchema;
}
