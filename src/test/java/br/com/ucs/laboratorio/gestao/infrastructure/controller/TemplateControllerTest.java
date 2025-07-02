package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.TemplateDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.TemplateResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.TemplateModel;
import br.com.ucs.laboratorio.gestao.domain.service.TemplateService;
import br.com.ucs.laboratorio.gestao.domain.type.TemplateType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TemplateControllerTest {

    @Mock
    private TemplateService templateService;

    @InjectMocks
    private TemplateController templateController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private TemplateDto templateDto;
    private TemplateResponse templateResponse;
    private TemplateModel template;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(templateController).build();
        objectMapper = new ObjectMapper();

        templateDto = new TemplateDto();
        templateDto.setDescription("Test Template");
        templateDto.setBrand("Test Brand");
        templateDto.setTemplateType(TemplateType.ANALOG);

        templateResponse = new TemplateResponse();
        templateResponse.setId(1L);
        templateResponse.setDescription("Test Template");
        templateResponse.setBrand("Test Brand");
        templateResponse.setTemplateType(TemplateType.ANALOG);

        template = new TemplateModel();
        template.setId(1L);
        template.setDescription("Test Template");
        template.setBrand("Test Brand");
        template.setTemplateType(TemplateType.ANALOG);
    }

    @Test
    void create_ShouldReturnTemplateResponse_WhenValidTemplateDto() throws Exception {
        when(templateService.create(any(TemplateDto.class))).thenReturn(templateResponse);

        mockMvc.perform(post("/template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateDto)))
                .andExpect(status().isOk());

        verify(templateService).create(any(TemplateDto.class));
    }

    @Test
    void findById_ShouldReturnTemplateResponse_WhenTemplateExists() throws Exception {
        when(templateService.findById(1L)).thenReturn(template);

        mockMvc.perform(get("/template/1"))
                .andExpect(status().isOk());

        verify(templateService).findById(1L);
    }

    @Test
    void findAll_ShouldReturnListOfTemplateResponse() throws Exception {
        List<TemplateResponse> templates = Arrays.asList(templateResponse);
        when(templateService.findAll()).thenReturn(templates);

        mockMvc.perform(get("/template"))
                .andExpect(status().isOk());

        verify(templateService).findAll();
    }

    @Test
    void update_ShouldReturnUpdatedTemplateResponse() throws Exception {
        when(templateService.update(eq(1L), any(TemplateDto.class))).thenReturn(templateResponse);

        mockMvc.perform(put("/template/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateDto)))
                .andExpect(status().isOk());

        verify(templateService).update(eq(1L), any(TemplateDto.class));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(templateService).delete(1L);

        mockMvc.perform(delete("/template/1"))
                .andExpect(status().isNoContent());

        verify(templateService).delete(1L);
    }

    @Test
    void filterTemplates_ShouldReturnFilteredTemplates_WithAllParameters() throws Exception {
        List<TemplateResponse> templates = Arrays.asList(templateResponse);
        when(templateService.filterTemplates("Test Brand", TemplateType.ANALOG, 1L))
                .thenReturn(templates);

        mockMvc.perform(get("/template/filter")
                        .param("brand", "Test Brand")
                        .param("templateType", String.valueOf(TemplateType.ANALOG))
                        .param("categoryId", "1"))
                .andExpect(status().isOk());

        verify(templateService).filterTemplates("Test Brand", TemplateType.ANALOG, 1L);
    }

    @Test
    void filterTemplates_ShouldReturnFilteredTemplates_WithoutOptionalParameters() throws Exception {
        List<TemplateResponse> templates = Arrays.asList(templateResponse);
        when(templateService.filterTemplates(isNull(), isNull(), isNull())).thenReturn(templates);

        mockMvc.perform(get("/template/filter"))
                .andExpect(status().isOk());

        verify(templateService).filterTemplates(isNull(), isNull(), isNull());
    }

    @Test
    void filterTemplates_ShouldReturnFilteredTemplates_WithOnlyBrand() throws Exception {
        List<TemplateResponse> templates = Arrays.asList(templateResponse);
        when(templateService.filterTemplates("Test Brand", null, null)).thenReturn(templates);

        mockMvc.perform(get("/template/filter")
                        .param("brand", "Test Brand"))
                .andExpect(status().isOk());

        verify(templateService).filterTemplates("Test Brand", null, null);
    }
}