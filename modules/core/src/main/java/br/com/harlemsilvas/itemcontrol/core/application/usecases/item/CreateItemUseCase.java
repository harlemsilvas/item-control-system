package br.com.harlemsilvas.itemcontrol.core.application.usecases.item;

import br.com.harlemsilvas.itemcontrol.core.application.ports.ItemRepository;
import br.com.harlemsilvas.itemcontrol.core.application.ports.TemplateRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Item;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Template;
import br.com.harlemsilvas.itemcontrol.core.domain.model.TemplateScope;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Use Case para criar um novo Item.
 */
public class CreateItemUseCase {

    private final ItemRepository itemRepository;
    private final TemplateRepository templateRepository;

    public CreateItemUseCase(ItemRepository itemRepository, TemplateRepository templateRepository) {
        this.itemRepository = Objects.requireNonNull(itemRepository, "ItemRepository cannot be null");
        this.templateRepository = Objects.requireNonNull(templateRepository, "TemplateRepository cannot be null");
    }

    /**
     * Executa o caso de uso de criação de item.
     *
     * @param item Item a ser criado
     * @return Item criado com ID gerado
     */
    public Item execute(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        // Validações de negócio já estão no construtor da entidade
        return itemRepository.save(item);
    }

    /**
     * Nova execução: cria item a partir de template existente ou cria novo template inline.
     *
     * Contrato:
     * - se newTemplate informado -> cria template (USER por padrão) e usa seu code
     * - senão, se templateId informado -> resolve template e usa seu code
     * - senão, usa templateCode informado (compatibilidade)
     */
    public Item execute(CreateItemCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }
        if (command.userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (command.name == null || command.name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        String templateCode = command.templateCode;

        if (command.newTemplate != null) {
            TemplateScope scope = command.newTemplate.scope != null ? command.newTemplate.scope : TemplateScope.USER;
            Template template = new Template.Builder()
                .userId(scope == TemplateScope.USER ? command.userId : null)
                .scope(scope)
                .code(command.newTemplate.code)
                .nameByLocale(command.newTemplate.nameByLocale)
                .descriptionByLocale(command.newTemplate.descriptionByLocale)
                .metadataSchema(command.newTemplate.metadataSchema)
                .build();

            Template created = templateRepository.save(template);
            templateCode = created.getCode();
        } else if (command.templateId != null) {
            Template resolved = templateRepository.findById(command.templateId)
                .orElseThrow(() -> new TemplateNotFoundException(command.templateId));
            templateCode = resolved.getCode();
        }

        if (templateCode == null || templateCode.trim().isEmpty()) {
            throw new IllegalArgumentException("TemplateCode cannot be null or empty");
        }

        Item item = new Item.Builder()
            .userId(command.userId)
            .name(command.name)
            .nickname(command.nickname)
            .categoryId(command.categoryId)
            .templateCode(templateCode)
            .tags(command.tags)
            .metadata(command.metadata)
            .build();

        return itemRepository.save(item);
    }

    public static class TemplateNotFoundException extends RuntimeException {
        public TemplateNotFoundException(UUID id) {
            super("Template not found: " + id);
        }
    }

    /**
     * Command para criação de item contendo dados de template.
     */
    public static class CreateItemCommand {
        public UUID userId;
        public String name;
        public String nickname;
        public UUID categoryId;
        public UUID templateId;
        public String templateCode;
        public TemplateCreateCommand newTemplate;
        public java.util.Set<String> tags;
        public Map<String, Object> metadata;

        public static class TemplateCreateCommand {
            public TemplateScope scope;
            public String code;
            public Map<String, String> nameByLocale;
            public Map<String, String> descriptionByLocale;
            public Map<String, Object> metadataSchema;
        }
    }
}
