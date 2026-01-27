package br.com.harlemsilvas.itemcontrol.api.web.controller;

import br.com.harlemsilvas.itemcontrol.api.web.dto.request.CreateItemRequest;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.item.CreateItemUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.item.GetItemByIdUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.item.ListUserItemsUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.item.UpdateItemMetadataUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.UUID;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@ContextConfiguration(classes = TestWebMvcConfig.class)
class ItemControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateItemUseCase createItemUseCase;

    @MockBean
    private GetItemByIdUseCase getItemByIdUseCase;

    @MockBean
    private ListUserItemsUseCase listUserItemsUseCase;

    @MockBean
    private UpdateItemMetadataUseCase updateItemMetadataUseCase;

    @Test
    void shouldReturn400WhenNoTemplateProvided() throws Exception {
        CreateItemRequest request = CreateItemRequest.builder()
            .userId(UUID.randomUUID())
            .name("Item sem template")
            .nickname("no-template")
            .metadata(new HashMap<>())
            // templateId/templateCode/newTemplate todos ausentes
            .build();

        mockMvc.perform(post("/api/v1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(createItemUseCase, never()).execute(org.mockito.ArgumentMatchers.any(CreateItemUseCase.CreateItemCommand.class));
    }
}
