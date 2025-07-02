package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.LaboratoryDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.LaboratoryEventTotalizerResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.LaboratoryResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.LaboratoryModel;
import br.com.ucs.laboratorio.gestao.domain.service.LaboratoryService;
import br.com.ucs.laboratorio.gestao.domain.type.EventType;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LaboratoryControllerTest {

    @Mock
    private LaboratoryService laboratoryService;

    @InjectMocks
    private LaboratoryController laboratoryController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private LaboratoryDto laboratoryDto;
    private LaboratoryResponse laboratoryResponse;
    private LaboratoryModel laboratory;
    private LaboratoryEventTotalizerResponse totalizerResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(laboratoryController).build();
        objectMapper = new ObjectMapper();

        laboratoryDto = new LaboratoryDto();
        laboratoryDto.setRoomName("Test Laboratory");
        laboratoryDto.setRoomNumber("Building A");

        laboratoryResponse = new LaboratoryResponse();
        laboratoryResponse.setId(1L);
        laboratoryResponse.setRoomName("Test Laboratory");
        laboratoryResponse.setRoomNumber("Building A");

        laboratory = new LaboratoryModel();
        laboratory.setId(1L);
        laboratory.setRoomName("Test Laboratory");
        laboratory.setRoomNumber("Building A");

        totalizerResponse = new LaboratoryEventTotalizerResponse();
        totalizerResponse.setEquipmentTotalizer(new ArrayList<>());
        totalizerResponse.setTotal(BigDecimal.TEN);
    }

    @Test
    void create_ShouldReturnLaboratoryResponse_WhenValidLaboratoryDto() throws Exception {
        when(laboratoryService.create(any(LaboratoryDto.class))).thenReturn(laboratoryResponse);

        mockMvc.perform(post("/laboratory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(laboratoryDto)))
                .andExpect(status().isOk());

        verify(laboratoryService).create(any(LaboratoryDto.class));
    }

    @Test
    void findById_ShouldReturnLaboratoryResponse_WhenLaboratoryExists() throws Exception {
        when(laboratoryService.findById(1L)).thenReturn(laboratory);

        mockMvc.perform(get("/laboratory/1"))
                .andExpect(status().isOk());

        verify(laboratoryService).findById(1L);
    }

    @Test
    void findAll_ShouldReturnListOfLaboratoryResponse() throws Exception {
        List<LaboratoryResponse> laboratories = Arrays.asList(laboratoryResponse);
        when(laboratoryService.findAll()).thenReturn(laboratories);

        mockMvc.perform(get("/laboratory"))
                .andExpect(status().isOk());

        verify(laboratoryService).findAll();
    }

    @Test
    void total_ShouldReturnTotalizerResponse_WithAllParameters() throws Exception {
        when(laboratoryService.totalEvents(eq(1L), eq(2L), eq(EventType.MAINTENANCE),
                any(LocalDate.class), any(LocalDate.class))).thenReturn(totalizerResponse);

        mockMvc.perform(get("/laboratory/total/1")
                        .param("categoryId", "2")
                        .param("eventType", "MAINTENANCE")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-12-31"))
                .andExpect(status().isOk());

        verify(laboratoryService).totalEvents(eq(1L), eq(2L), eq(EventType.MAINTENANCE),
                any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void total_ShouldReturnTotalizerResponse_WithoutOptionalParameters() throws Exception {
        when(laboratoryService.totalEvents(eq(1L), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(totalizerResponse);

        mockMvc.perform(get("/laboratory/total/1"))
                .andExpect(status().isOk());

        verify(laboratoryService).totalEvents(eq(1L), isNull(), isNull(), isNull(), isNull());
    }

    @Test
    void update_ShouldReturnUpdatedLaboratoryResponse() throws Exception {
        when(laboratoryService.update(eq(1L), any(LaboratoryDto.class))).thenReturn(laboratoryResponse);

        mockMvc.perform(put("/laboratory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(laboratoryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(laboratoryService).update(eq(1L), any(LaboratoryDto.class));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(laboratoryService).delete(1L);

        mockMvc.perform(delete("/laboratory/1"))
                .andExpect(status().isNoContent());

        verify(laboratoryService).delete(1L);
    }
}
