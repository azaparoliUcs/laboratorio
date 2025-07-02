package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import br.com.ucs.laboratorio.gestao.domain.dto.EquipmentDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EquipmentResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.EquipmentModel;
import br.com.ucs.laboratorio.gestao.domain.service.EquipmentService;
import br.com.ucs.laboratorio.gestao.domain.type.EquipmentStatusType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EquipmentControllerTest {

    @Mock
    private EquipmentService equipmentService;

    @InjectMocks
    private EquipmentController equipmentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private EquipmentDto equipmentDto;
    private EquipmentResponse equipmentResponse;
    private EquipmentModel equipment;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(equipmentController).build();
        objectMapper = new ObjectMapper();

        equipmentDto = new EquipmentDto();
        equipmentDto.setDescription("Microscópio óptico");
        equipmentDto.setEquipmentStatusType(EquipmentStatusType.AVAILABLE);
        equipmentDto.setLaboratoryId(1L);
        equipmentDto.setTemplateId(1L);

        equipmentResponse = new EquipmentResponse();
        equipmentResponse.setId(1L);
        equipmentResponse.setDescription("Microscópio óptico");
        equipmentResponse.setEquipmentStatusType(EquipmentStatusType.AVAILABLE);

        equipment = new EquipmentModel();
        equipment.setId(1L);
        equipment.setDescription("Microscópio óptico");
        equipment.setEquipmentStatusType(EquipmentStatusType.AVAILABLE);
    }

    @Test
    void shouldCreateEquipmentSuccessfully() throws Exception {
        when(equipmentService.create(any(EquipmentDto.class))).thenReturn(equipmentResponse);

        mockMvc.perform(post("/equipment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipmentDto)))
                .andExpect(status().isOk());

        verify(equipmentService).create(any(EquipmentDto.class));
    }

    @Test
    void shouldFindEquipmentByIdSuccessfully() throws Exception {
        try (MockedStatic<MapperUtil> mapperUtil = mockStatic(MapperUtil.class)) {
            when(equipmentService.findById(1L)).thenReturn(equipment);
            mapperUtil.when(() -> MapperUtil.mapObject(equipment, EquipmentResponse.class))
                    .thenReturn(equipmentResponse);

            mockMvc.perform(get("/equipment/1"))
                    .andExpect(status().isOk());

            verify(equipmentService).findById(1L);
        }
    }

    @Test
    void shouldFindAllEquipmentsSuccessfully() throws Exception {
        List<EquipmentResponse> equipments = Arrays.asList(equipmentResponse);
        when(equipmentService.findAll()).thenReturn(equipments);

        mockMvc.perform(get("/equipment"))
                .andExpect(status().isOk());

        verify(equipmentService).findAll();
    }

    @Test
    void shouldFindEquipmentsByLaboratorySuccessfully() throws Exception {
        List<EquipmentResponse> equipments = Arrays.asList(equipmentResponse);
        when(equipmentService.findByLaboratoryId(1L)).thenReturn(equipments);

        mockMvc.perform(get("/equipment/laboratory/1"))
                .andExpect(status().isOk());

        verify(equipmentService).findByLaboratoryId(1L);
    }

    @Test
    void shouldFindExpirationEquipmentWithoutIdSuccessfully() throws Exception {
        List<EquipmentResponse> equipments = Arrays.asList(equipmentResponse);
        when(equipmentService.findExpirationEquipment(null)).thenReturn(equipments);

        mockMvc.perform(get("/equipment/expiration"))
                .andExpect(status().isOk());

        verify(equipmentService).findExpirationEquipment(null);
    }

    @Test
    void shouldFindExpirationEquipmentWithIdSuccessfully() throws Exception {
        List<EquipmentResponse> equipments = Arrays.asList(equipmentResponse);
        when(equipmentService.findExpirationEquipment(1L)).thenReturn(equipments);

        mockMvc.perform(get("/equipment/expiration?id=1"))
                .andExpect(status().isOk());

        verify(equipmentService).findExpirationEquipment(1L);
    }

    @Test
    void shouldUpdateEquipmentSuccessfully() throws Exception {
        when(equipmentService.update(eq(1L), any(EquipmentDto.class))).thenReturn(equipmentResponse);

        mockMvc.perform(put("/equipment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipmentDto)))
                .andExpect(status().isOk());

        verify(equipmentService).update(eq(1L), any(EquipmentDto.class));
    }

    @Test
    void shouldFilterEquipmentsWithAllParametersSuccessfully() throws Exception {
        List<EquipmentResponse> equipments = Arrays.asList(equipmentResponse);
        when(equipmentService.filter(1L, 1L, EquipmentStatusType.AVAILABLE)).thenReturn(equipments);

        mockMvc.perform(get("/equipment/filter")
                        .param("laboratoryId", "1")
                        .param("categoryId", "1")
                        .param("status", "AVAILABLE"))
                .andExpect(status().isOk());

        verify(equipmentService).filter(1L, 1L, EquipmentStatusType.AVAILABLE);
    }

    @Test
    void shouldFilterEquipmentsWithRequiredParametersOnlySuccessfully() throws Exception {
        List<EquipmentResponse> equipments = Arrays.asList(equipmentResponse);
        when(equipmentService.filter(1L, null, null)).thenReturn(equipments);

        mockMvc.perform(get("/equipment/filter")
                        .param("laboratoryId", "1"))
                .andExpect(status().isOk());

        verify(equipmentService).filter(1L, null, null);
    }

    @Test
    void shouldDeleteEquipmentSuccessfully() throws Exception {
        mockMvc.perform(delete("/equipment/1"))
                .andExpect(status().isNoContent());

        verify(equipmentService).delete(1L);
    }

    @Test
    void shouldHandleInvalidEquipmentStatusInFilter() throws Exception {
        mockMvc.perform(get("/equipment/filter")
                        .param("laboratoryId", "1")
                        .param("status", "INVALID_STATUS"))
                .andExpect(status().isBadRequest());
    }
}