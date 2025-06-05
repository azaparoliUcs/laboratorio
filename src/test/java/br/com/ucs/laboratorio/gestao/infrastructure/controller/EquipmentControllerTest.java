package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.EquipmentDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EquipmentResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.EquipmentModel;
import br.com.ucs.laboratorio.gestao.domain.service.EquipmentService;
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

public class EquipmentControllerTest {

    @InjectMocks
    private EquipmentController equipmentController;

    @Mock
    private EquipmentService equipmentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        EquipmentDto dto = new EquipmentDto();
        EquipmentResponse response = new EquipmentResponse();

        when(equipmentService.create(dto)).thenReturn(response);

        var result = equipmentController.create(dto);

        assertEquals(response, result.getBody());
    }

    @Test
    void testFindById() {
        try (var mocked = mockStatic(MapperUtil.class)) {
            when(equipmentService.findById(1L)).thenReturn(new EquipmentModel());
            mocked.when(() -> MapperUtil.mapObject(any(), eq(EquipmentResponse.class))).thenReturn(new EquipmentResponse());

            var result = equipmentController.findById(1L);
            assertNotNull(result.getBody());
        }
    }

    @Test
    void testFindAll() {
        when(equipmentService.findAll()).thenReturn(List.of(new EquipmentResponse()));
        var result = equipmentController.findAll();
        assertEquals(1, result.getBody().size());
    }

    @Test
    void testFindByLaboratory() {
        when(equipmentService.findByLaboratoryId(1L)).thenReturn(List.of(new EquipmentResponse()));
        var result = equipmentController.findByLaboratory(1L);
        assertEquals(1, result.getBody().size());
    }

    @Test
    void testFindExpirationEquipment() {
        when(equipmentService.findExpirationEquipment(1L)).thenReturn(List.of(new EquipmentResponse()));
        var result = equipmentController.findExirationEquipment(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testUpdate() {
        EquipmentDto dto = new EquipmentDto();
        when(equipmentService.update(1L, dto)).thenReturn(new EquipmentResponse());
        var result = equipmentController.update(1L, dto);
        assertNotNull(result.getBody());
    }

    @Test
    void testDelete() {
        var result = equipmentController.delete(1L);
        assertEquals(204, result.getStatusCodeValue());
        verify(equipmentService).delete(1L);
    }
}
