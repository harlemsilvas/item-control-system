package br.com.harlemsilvas.itemcontrol.api.controllers;

import br.com.harlemsilvas.itemcontrol.api.dto.category.CategoryResponse;
import br.com.harlemsilvas.itemcontrol.api.dto.category.CreateCategoryRequest;
import br.com.harlemsilvas.itemcontrol.api.dto.category.UpdateCategoryRequest;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Category;
import br.com.harlemsilvas.itemcontrol.core.usecases.category.CreateCategoryUseCase;
import br.com.harlemsilvas.itemcontrol.core.usecases.category.DeleteCategoryUseCase;
import br.com.harlemsilvas.itemcontrol.core.usecases.category.GetCategoriesByUserUseCase;
import br.com.harlemsilvas.itemcontrol.core.usecases.category.UpdateCategoryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller REST para gerenciamento de categorias.
 *
 * @author Harlem Silva
 * @since 2026-01-23
 */
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "Gerenciamento de categorias")
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoriesByUserUseCase getCategoriesByUserUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase,
                            GetCategoriesByUserUseCase getCategoriesByUserUseCase,
                            UpdateCategoryUseCase updateCategoryUseCase,
                            DeleteCategoryUseCase deleteCategoryUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.getCategoriesByUserUseCase = getCategoriesByUserUseCase;
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
    }

    /**
     * Cria uma nova categoria.
     *
     * @param request os dados da categoria
     * @return a categoria criada
     */
    @PostMapping
    @Operation(summary = "Criar categoria", description = "Cria uma nova categoria")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        Category category = new Category.Builder()
                .userId(request.userId())
                .name(request.name())
                .parentId(request.parentId())
                .build();

        Category createdCategory = createCategoryUseCase.execute(category);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CategoryResponse.from(createdCategory));
    }

    /**
     * Busca categorias por usuário.
     *
     * @param userId o ID do usuário
     * @return lista de categorias do usuário
     */
    @GetMapping
    @Operation(summary = "Buscar categorias por usuário", description = "Busca todas as categorias de um usuário")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByUser(@RequestParam UUID userId) {
        List<Category> categories = getCategoriesByUserUseCase.execute(userId);

        List<CategoryResponse> responses = categories.stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * Atualiza uma categoria existente.
     *
     * @param id o ID da categoria
     * @param request os novos dados da categoria
     * @return a categoria atualizada
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria", description = "Atualiza uma categoria existente")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCategoryRequest request) {

        Category updatedCategory = updateCategoryUseCase.execute(id, request.name());

        return ResponseEntity.ok(CategoryResponse.from(updatedCategory));
    }

    /**
     * Deleta uma categoria.
     *
     * @param id o ID da categoria
     * @return resposta vazia
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar categoria", description = "Deleta uma categoria")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        deleteCategoryUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
