package br.com.harlemsilvas.itemcontrol.api.controllers;

import br.com.harlemsilvas.itemcontrol.api.web.dto.request.CreateItemRequest;
import br.com.harlemsilvas.itemcontrol.api.web.dto.response.ItemResponse;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
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

        CreateItemRequest request = CreateItemRequest.builder()
                .userId(userId)
                .name("Meu Carro")
                .nickname("corolla-2020")
                .templateCode("VEHICLE")
                .metadata(metadata)
                .build();

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
        assertThat(response.getId()).isNotNull();
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getStatus()).isNotNull();
    }

    @Test
    @DisplayName("Should get item by id")
    void shouldGetItemById() throws Exception {
        // Given - Create an item first
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("test", "value");

        CreateItemRequest createRequest = CreateItemRequest.builder()
                .userId(userId)
                .name("Test Item")
                .nickname("test-001")
                .templateCode("GENERAL")
                .metadata(metadata)
                .build();

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
        mockMvc.perform(get("/api/v1/items/" + createdItem.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdItem.getId().toString()))
                .andExpect(jsonPath("$.name").value("Test Item"))
                .andExpect(jsonPath("$.nickname").value("test-001"));
    }

    @Test
    @DisplayName("Should list user items")
    void shouldListUserItems() throws Exception {
        // Given - Create multiple items
        for (int i = 1; i <= 3; i++) {
            CreateItemRequest request = CreateItemRequest.builder()
                    .userId(userId)
                    .name("Item " + i)
                    .nickname("item-" + i)
                    .templateCode("GENERAL")
                    .metadata(new HashMap<>())
                    .build();

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
        CreateItemRequest request = CreateItemRequest.builder()
                .userId(null) // userId is required
                .name("")     // name cannot be empty
                .nickname("test")
                .templateCode("GENERAL")
                .metadata(new HashMap<>())
                .build();

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
