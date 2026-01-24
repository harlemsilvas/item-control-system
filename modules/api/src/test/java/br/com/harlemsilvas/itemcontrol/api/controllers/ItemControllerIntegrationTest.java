package br.com.harlemsilvas.itemcontrol.api.controllers;

import br.com.harlemsilvas.itemcontrol.api.TestContainersConfiguration;
import br.com.harlemsilvas.itemcontrol.api.dto.item.CreateItemRequest;
import br.com.harlemsilvas.itemcontrol.api.dto.item.ItemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Import(TestContainersConfiguration.class)
@DisplayName("ItemController Integration Tests")
class ItemControllerIntegrationTest {

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
    @DisplayName("Should create item successfully")
    void shouldCreateItemSuccessfully() throws Exception {
        // Given
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("brand", "Toyota");
        metadata.put("model", "Corolla");

        CreateItemRequest request = new CreateItemRequest(
                userId,
                "Meu Carro",
                "corolla-2020",
                "VEHICLE",
                null,
                null,
                metadata
        );

        String requestBody = objectMapper.writeValueAsString(request);

        // When
        MvcResult result = mockMvc.perform(post("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Meu Carro"))
                .andExpect(jsonPath("$.nickname").value("corolla-2020"))
                .andExpect(jsonPath("$.templateCode").value("VEHICLE"))
                .andReturn();

        // Then
        String responseBody = result.getResponse().getContentAsString();
        ItemResponse response = objectMapper.readValue(responseBody, ItemResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.active()).isTrue();
    }

    @Test
    @DisplayName("Should get item by id")
    void shouldGetItemById() throws Exception {
        // Given - Create an item first
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("test", "value");

        CreateItemRequest createRequest = new CreateItemRequest(
                userId,
                "Test Item",
                "test-001",
                "GENERAL",
                null,
                null,
                metadata
        );

        String createBody = objectMapper.writeValueAsString(createRequest);

        MvcResult createResult = mockMvc.perform(post("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andReturn();

        ItemResponse createdItem = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                ItemResponse.class
        );

        // When - Get the item
        mockMvc.perform(get("/api/v1/items/" + createdItem.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdItem.id().toString()))
                .andExpect(jsonPath("$.name").value("Test Item"))
                .andExpect(jsonPath("$.nickname").value("test-001"));
    }

    @Test
    @DisplayName("Should list user items")
    void shouldListUserItems() throws Exception {
        // Given - Create multiple items
        for (int i = 1; i <= 3; i++) {
            CreateItemRequest request = new CreateItemRequest(
                    userId,
                    "Item " + i,
                    "item-" + i,
                    "GENERAL",
                    null,
                    null,
                    new HashMap<>()
            );

            mockMvc.perform(post("/api/v1/items")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        // When
        mockMvc.perform(get("/api/v1/items")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[1].name").exists())
                .andExpect(jsonPath("$[2].name").exists());
    }

    @Test
    @DisplayName("Should return 400 when creating item with invalid data")
    void shouldReturn400WhenCreatingItemWithInvalidData() throws Exception {
        // Given - Invalid request (missing required fields)
        CreateItemRequest request = new CreateItemRequest(
                null, // userId is required
                "",   // name cannot be empty
                "test",
                "GENERAL",
                null,
                null,
                new HashMap<>()
        );

        String requestBody = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(post("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 when item not found")
    void shouldReturn404WhenItemNotFound() throws Exception {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When & Then
        mockMvc.perform(get("/api/v1/items/" + nonExistentId))
                .andExpect(status().isNotFound());
    }
}
