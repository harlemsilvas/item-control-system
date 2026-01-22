package br.com.harlemsilvas.itemcontrol.api.infra.mongo.adapter;

import br.com.harlemsilvas.itemcontrol.api.infra.mongo.document.ItemDocument;
import br.com.harlemsilvas.itemcontrol.api.infra.mongo.mapper.ItemDocumentMapper;
import br.com.harlemsilvas.itemcontrol.api.infra.mongo.repository.SpringDataItemRepository;
import br.com.harlemsilvas.itemcontrol.core.application.ports.ItemRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.enums.ItemStatus;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter que implementa ItemRepository usando MongoDB.
 */
@Repository
@RequiredArgsConstructor
public class MongoItemRepositoryAdapter implements ItemRepository {

    private final SpringDataItemRepository springDataRepository;
    private final ItemDocumentMapper mapper;

    @Override
    public Item save(Item item) {
        ItemDocument document = mapper.toDocument(item);
        ItemDocument saved = springDataRepository.save(document);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Item> findById(UUID id) {
        return springDataRepository.findById(ItemDocument.toStringId(id))
            .map(mapper::toDomain);
    }

    @Override
    public List<Item> findByUserId(UUID userId) {
        return springDataRepository.findByUserId(ItemDocument.toStringId(userId))
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Item> findByUserIdAndStatus(UUID userId, ItemStatus status) {
        return springDataRepository.findByUserIdAndStatus(ItemDocument.toStringId(userId), status)
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Item> findByUserIdAndTemplateCode(UUID userId, String templateCode) {
        return springDataRepository.findByUserIdAndTemplateCode(ItemDocument.toStringId(userId), templateCode)
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Item> findByUserIdAndTags(UUID userId, Set<String> tags) {
        return springDataRepository.findByUserIdAndTagsIn(ItemDocument.toStringId(userId), tags)
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Item> findByCategoryId(UUID categoryId) {
        return springDataRepository.findByCategoryId(ItemDocument.toStringId(categoryId))
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        springDataRepository.deleteById(ItemDocument.toStringId(id));
    }

    @Override
    public boolean existsById(UUID id) {
        return springDataRepository.existsById(ItemDocument.toStringId(id));
    }

    @Override
    public long countByUserId(UUID userId) {
        return springDataRepository.countByUserId(ItemDocument.toStringId(userId));
    }
}
