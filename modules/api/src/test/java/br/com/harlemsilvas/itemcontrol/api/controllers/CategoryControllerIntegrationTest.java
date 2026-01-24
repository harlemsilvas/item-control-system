package br.com.harlemsilvas.itemcontrol.api.controllers;

import br.com.harlemsilvas.itemcontrol.api.dto.category.CreateCategoryRequest;
import br.com.harlemsilvas.itemcontrol.api.dto.category.CategoryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("CategoryController Integration Tests")
class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should create category successfully")
    void shouldCreateCategorySuccessfully() throws Exception {
        // Given
        CreateCategoryRequest request = new CreateCategoryRequest(
                userId,
                "Veiculos",
                null
        );

        String requestBody = objectMapper.writeValueAsString(request);

        // When
        MvcResult result = mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Veiculos"))
                .andReturn();

        // Then
        String responseBody = result.getResponse().getContentAsString();
        CategoryResponse response = objectMapper.readValue(responseBody, CategoryResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.name()).isEqualTo("Veiculos");
    }

    @Test
    @DisplayName("Should list user categories")
    void shouldListUserCategories() throws Exception {
        // Given - Create multiple categories
        String[] categoryNames = {"Veiculos", "Eletronicos", "Imoveis"};

        for (String name : categoryNames) {
            CreateCategoryRequest request = new CreateCategoryRequest(userId, name, null);
            mockMvc.perform(post("/api/v1/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        // When
        mockMvc.perform(get("/api/v1/categories")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[1].name").exists())
                .andExpect(jsonPath("$[2].name").exists());
    }

    @Test
    @DisplayName("Should update category name")
    void shouldUpdateCategoryName() throws Exception {
        // Given - Create a category first
        CreateCategoryRequest createRequest = new CreateCategoryRequest(
                userId,
                "Old Name",
                null
        );

        MvcResult createResult = mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryResponse createdCategory = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                CategoryResponse.class
        );

        // When - Update the category
        String updateJson = "{\"name\":\"New Name\"}";

        mockMvc.perform(put("/api/v1/categories/" + createdCategory.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.id").value(createdCategory.id().toString()));
    }

    @Test
    @DisplayName("Should delete category")
    void shouldDeleteCategory() throws Exception {
        // Given - Create a category
        CreateCategoryRequest request = new CreateCategoryRequest(
                userId,
                "To Delete",
                null
        );

        MvcResult createResult = mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryResponse createdCategory = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                CategoryResponse.class
        );

        // When - Delete the category
        mockMvc.perform(delete("/api/v1/categories/" + createdCategory.id()))
                .andExpect(status().isNoContent());

        // Then - Verify it's deleted
        mockMvc.perform(get("/api/v1/categories")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Should return 400 when creating category with invalid data")
    void shouldReturn400WhenCreatingCategoryWithInvalidData() throws Exception {
        // Given - Invalid request (missing required fields)
        CreateCategoryRequest request = new CreateCategoryRequest(
                null, // userId is required
                "",   // name cannot be empty
                null
        );

        String requestBody = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
