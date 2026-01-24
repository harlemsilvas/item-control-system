package br.com.harlemsilvas.itemcontrol.api.adapters.persistence.mongodb;

import br.com.harlemsilvas.itemcontrol.api.adapters.persistence.mongodb.mapper.CategoryDocumentMapper;
import br.com.harlemsilvas.itemcontrol.api.adapters.persistence.mongodb.repository.SpringDataCategoryRepository;
import br.com.harlemsilvas.itemcontrol.core.application.ports.CategoryRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter MongoDB para CategoryRepository.
 *
 * @author Harlem Silva
 * @since 2026-01-23
 */
@Component
public class MongoCategoryRepositoryAdapter implements CategoryRepository {

    private final SpringDataCategoryRepository springDataRepository;
    private final CategoryDocumentMapper mapper;

    public MongoCategoryRepositoryAdapter(SpringDataCategoryRepository springDataRepository,
                                         CategoryDocumentMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Category save(Category category) {
        var document = mapper.toDocument(category);
        var savedDocument = springDataRepository.save(document);
        return mapper.toDomain(savedDocument);
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return springDataRepository.findById(id.toString())
                .map(mapper::toDomain);
    }

    @Override
    public List<Category> findByUserId(UUID userId) {
        return springDataRepository.findByUserId(userId.toString())
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findRootByUserId(UUID userId) {
        return springDataRepository.findByUserIdAndParentIdIsNull(userId.toString())
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findByParentId(UUID parentId) {
        return springDataRepository.findByParentId(parentId.toString())
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        springDataRepository.deleteById(id.toString());
    }

    @Override
    public boolean existsById(UUID id) {
        return springDataRepository.existsById(id.toString());
    }

    @Override
    public boolean hasChildren(UUID categoryId) {
        return springDataRepository.existsByParentId(categoryId.toString());
    }

    @Override
    public long countByUserId(UUID userId) {
        return springDataRepository.countByUserId(userId.toString());
    }
}
