package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.TemplateDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.TemplateResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.TemplateModel;
import br.com.ucs.laboratorio.gestao.domain.service.TemplateService;
import br.com.ucs.laboratorio.gestao.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TemplateControllerTest {

    @InjectMocks
    private TemplateController templateController;

    @Mock
    private TemplateService templateService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        TemplateDto dto = new TemplateDto();
        TemplateResponse response = new TemplateResponse();

        when(templateService.create(dto)).thenReturn(response);

        var result = templateController.create(dto);
        assertEquals(response, result.getBody());
    }

    @Test
    void testFindById() {
        try (var mocked = mockStatic(MapperUtil.class)) {
            when(templateService.findById(1L)).thenReturn(new TemplateModel());
            mocked.when(() -> MapperUtil.mapObject(any(), eq(TemplateResponse.class))).thenReturn(new TemplateResponse());

            var result = templateController.findById(1L);
            assertNotNull(result.getBody());
        }
    }

    @Test
    void testFindAll() {
        when(templateService.findAll()).thenReturn(List.of(new TemplateResponse()));
        var result = templateController.findAll();
        assertEquals(1, result.getBody().size());
    }

    @Test
    void testUpdate() {
        TemplateDto dto = new TemplateDto();
        when(templateService.update(1L, dto)).thenReturn(new TemplateResponse());
        var result = templateController.update(1L, dto);
        assertNotNull(result.getBody());
    }

    @Test
    void testDelete() {
        var result = templateController.delete(1L);
        assertEquals(204, result.getStatusCodeValue());
        verify(templateService).delete(1L);
    }
}