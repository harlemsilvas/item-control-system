package br.com.harlemsilvas.itemcontrol.api.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * DTO para requisição de criação de Item.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequest {

    @NotNull(message = "UserId cannot be null")
    private UUID userId;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String nickname;

    private UUID categoryId;

    /**
     * Template existente por ID (nova opção).
     */
    private UUID templateId;

    /**
     * Template existente por código (compatibilidade).
     * Opcional se templateId ou newTemplate forem informados.
     */
    private String templateCode;

    /**
     * Novo template criado junto com o item.
     */
    private CreateItemTemplateRequest newTemplate;

    private Set<String> tags;

    private Map<String, Object> metadata;
}
