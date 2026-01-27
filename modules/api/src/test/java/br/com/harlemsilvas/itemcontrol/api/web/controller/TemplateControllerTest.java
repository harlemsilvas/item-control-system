package br.com.harlemsilvas.itemcontrol.api.web.controller;

import br.com.harlemsilvas.itemcontrol.api.web.dto.request.CreateTemplateRequest;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.template.CreateTemplateUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.template.DeleteTemplateUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.template.GetTemplateByIdUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.template.ListTemplatesUseCase;
import br.com.harlemsilvas.itemcontrol.core.application.usecases.template.UpdateTemplateUseCase;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Template;
import br.com.harlemsilvas.itemcontrol.core.domain.model.TemplateScope;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TemplateController.class)
@ContextConfiguration(classes = TestWebMvcConfig.class)
class TemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateTemplateUseCase createTemplateUseCase;

    @MockBean
    private UpdateTemplateUseCase updateTemplateUseCase;

    @MockBean
    private DeleteTemplateUseCase deleteTemplateUseCase;

    @MockBean
    private GetTemplateByIdUseCase getTemplateByIdUseCase;

    @MockBean
    private ListTemplatesUseCase listTemplatesUseCase;

    @Test
    void shouldCreateTemplateGlobal() throws Exception {
        UUID id = UUID.randomUUID();

        Template created = new Template.Builder()
            .id(id)
            .scope(TemplateScope.GLOBAL)
            .code("VEHICLE")
            .nameByLocale(Map.of("pt-BR", "Veículo"))
            .descriptionByLocale(Map.of("pt-BR", "Template de veículo"))
            .metadataSchema(Map.of("brand", Map.of("type", "string")))
            .build();

        when(createTemplateUseCase.execute(any(Template.class))).thenReturn(created);

        CreateTemplateRequest request = CreateTemplateRequest.builder()
            .scope(TemplateScope.GLOBAL)
            .code("VEHICLE")
            .nameByLocale(Map.of("pt-BR", "Veículo"))
            .descriptionByLocale(Map.of("pt-BR", "Template de veículo"))
            .metadataSchema(Map.of("brand", Map.of("type", "string")))
            .build();

        mockMvc.perform(post("/api/v1/templates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.code").value("VEHICLE"))
            .andExpect(jsonPath("$.scope").value("GLOBAL"))
            .andExpect(jsonPath("$.name").value("Veículo"));

        ArgumentCaptor<Template> captor = ArgumentCaptor.forClass(Template.class);
        verify(createTemplateUseCase).execute(captor.capture());

        Template passed = captor.getValue();
        assertThat(passed.getScope()).isEqualTo(TemplateScope.GLOBAL);
        assertThat(passed.getUserId()).isNull();
        assertThat(passed.getCode()).isEqualTo("VEHICLE");
    }

    @Test
    void shouldReturn404WhenGetByIdNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(getTemplateByIdUseCase.execute(id)).thenReturn(Optional.empty());

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/v1/templates/" + id))
            .andExpect(status().isNotFound());
    }
}
