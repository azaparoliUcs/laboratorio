package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.LaboratoryDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.LaboratoryResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.LaboratoryModel;
import br.com.ucs.laboratorio.gestao.domain.service.LaboratoryService;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class LaboratoryControllerTest {

    @InjectMocks
    private LaboratoryController laboratoryController;

    @Mock
    private LaboratoryService laboratoryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        LaboratoryDto dto = new LaboratoryDto();
        LaboratoryResponse response = new LaboratoryResponse();

        when(laboratoryService.create(dto)).thenReturn(response);

        var result = laboratoryController.create(dto);
        assertEquals(response, result.getBody());
    }

    @Test
    void testFindById() {
        try (var mocked = mockStatic(MapperUtil.class)) {
            when(laboratoryService.findById(1L)).thenReturn(new LaboratoryModel());
            mocked.when(() -> MapperUtil.mapObject(any(), eq(LaboratoryResponse.class))).thenReturn(new LaboratoryResponse());

            var result = laboratoryController.findById(1L);
            assertNotNull(result.getBody());
        }
    }

    @Test
    void testFindAll() {
        when(laboratoryService.findAll()).thenReturn(List.of(new LaboratoryResponse()));
        var result = laboratoryController.findAll();
        assertEquals(1, result.getBody().size());
    }

    @Test
    void testUpdate() {
        LaboratoryDto dto = new LaboratoryDto();
        when(laboratoryService.update(1L, dto)).thenReturn(new LaboratoryResponse());
        var result = laboratoryController.update(1L, dto);
        assertNotNull(result.getBody());
    }

    @Test
    void testDelete() {
        var result = laboratoryController.delete(1L);
        assertEquals(204, result.getStatusCodeValue());
        verify(laboratoryService).delete(1L);
    }
}