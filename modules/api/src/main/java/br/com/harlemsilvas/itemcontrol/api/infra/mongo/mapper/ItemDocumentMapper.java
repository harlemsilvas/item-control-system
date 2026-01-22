package br.com.harlemsilvas.itemcontrol.api.infra.mongo.mapper;

import br.com.harlemsilvas.itemcontrol.api.infra.mongo.document.ItemDocument;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Item;
import org.springframework.stereotype.Component;

/**
 * Mapper para converter entre Item (domínio) e ItemDocument (MongoDB).
 */
@Component
public class ItemDocumentMapper {

    /**
     * Converte entidade de domínio para documento MongoDB.
     */
    public ItemDocument toDocument(Item item) {
        if (item == null) {
            return null;
        }

        return ItemDocument.builder()
            .id(ItemDocument.toStringId(item.getId()))
            .userId(ItemDocument.toStringId(item.getUserId()))
            .name(item.getName())
            .nickname(item.getNickname())
            .categoryId(ItemDocument.toStringId(item.getCategoryId()))
            .templateCode(item.getTemplateCode())
            .status(item.getStatus())
            .tags(item.getTags())
            .metadata(item.getMetadata())
            .createdAt(item.getCreatedAt())
            .updatedAt(item.getUpdatedAt())
            .build();
    }

    /**
     * Converte documento MongoDB para entidade de domínio.
     */
    public Item toDomain(ItemDocument document) {
        if (document == null) {
            return null;
        }

        return new Item.Builder()
            .id(ItemDocument.toUUID(document.getId()))
            .userId(ItemDocument.toUUID(document.getUserId()))
            .name(document.getName())
            .nickname(document.getNickname())
            .categoryId(ItemDocument.toUUID(document.getCategoryId()))
            .templateCode(document.getTemplateCode())
            .status(document.getStatus())
            .tags(document.getTags())
            .metadata(document.getMetadata())
            .createdAt(document.getCreatedAt())
            .updatedAt(document.getUpdatedAt())
            .build();
    }
}
