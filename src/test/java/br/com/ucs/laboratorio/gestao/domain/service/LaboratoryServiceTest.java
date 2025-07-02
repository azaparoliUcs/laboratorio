package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dao.EquipmentDao;
import br.com.ucs.laboratorio.gestao.domain.dao.EventDao;
import br.com.ucs.laboratorio.gestao.domain.dto.LaboratoryDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventTotalizerItemsResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventTotalizerResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.LaboratoryEventTotalizerResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.LaboratoryResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.BlockModel;
import br.com.ucs.laboratorio.gestao.domain.entity.EquipmentModel;
import br.com.ucs.laboratorio.gestao.domain.entity.EventModel;
import br.com.ucs.laboratorio.gestao.domain.entity.LaboratoryModel;
import br.com.ucs.laboratorio.gestao.application.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.domain.type.EventType;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.LaboratoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LaboratoryServiceTest {

    @Mock
    private LaboratoryRepository laboratoryRepository;

    @Mock
    private BlockService blockService;

    @Mock
    private EventService eventService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EquipmentDao equipmentDao;

    @Mock
    private EventDao eventDao;

    @InjectMocks
    private LaboratoryService laboratoryService;

    private LaboratoryDto laboratoryDto;
    private LaboratoryModel laboratoryModel;
    private LaboratoryResponse laboratoryResponse;
    private BlockModel blockModel;

    @BeforeEach
    void setUp() {
        laboratoryDto = new LaboratoryDto();
        laboratoryDto.setBlockId(1L);
        laboratoryDto.setRoomNumber("101");
        laboratoryDto.setRoomName("Lab Physics");

        blockModel = new BlockModel();
        blockModel.setId(1L);

        laboratoryModel = new LaboratoryModel();
        laboratoryModel.setId(1L);
        laboratoryModel.setRoomNumber("101");
        laboratoryModel.setRoomName("Lab Physics");
        laboratoryModel.setBlock(blockModel);

        laboratoryResponse = new LaboratoryResponse();
        laboratoryResponse.setId(1L);
        laboratoryResponse.setRoomNumber("101");
        laboratoryResponse.setRoomName("Lab Physics");
    }

    @Test
    void create_ShouldCreateLaboratory_WhenValidData() {
        // Given
        when(blockService.findById(1L)).thenReturn(blockModel);
        when(modelMapper.map(laboratoryDto, LaboratoryModel.class)).thenReturn(laboratoryModel);
        when(laboratoryRepository.save(any(LaboratoryModel.class))).thenReturn(laboratoryModel);
        when(modelMapper.map(laboratoryModel, LaboratoryResponse.class)).thenReturn(laboratoryResponse);

        // When
        LaboratoryResponse result = laboratoryService.create(laboratoryDto);

        // Then
        assertNotNull(result);
        assertEquals(laboratoryResponse.getId(), result.getId());
        assertEquals(laboratoryResponse.getRoomNumber(), result.getRoomNumber());
        assertEquals(laboratoryResponse.getRoomName(), result.getRoomName());

        verify(blockService).findById(1L);
        verify(laboratoryRepository).save(any(LaboratoryModel.class));
        verify(modelMapper, times(2)).map(any(), any());
    }

    @Test
    void findById_ShouldReturnLaboratory_WhenExists() {
        // Given
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.of(laboratoryModel));

        // When
        LaboratoryModel result = laboratoryService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(laboratoryModel.getId(), result.getId());
        verify(laboratoryRepository).findById(1L);
    }

    @Test
    void findById_ShouldThrowBusinessException_WhenNotExists() {
        // Given
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> laboratoryService.findById(1L));
        assertEquals("Laboratorio nao existe", exception.getMessage());
        verify(laboratoryRepository).findById(1L);
    }

    @Test
    void findAll_ShouldReturnAllLaboratories() {
        // Given
        List<LaboratoryModel> laboratories = Arrays.asList(laboratoryModel);
        when(laboratoryRepository.findAll()).thenReturn(laboratories);

        // When
        List<LaboratoryResponse> result = laboratoryService.findAll();

        // Then
        assertNotNull(result);
        verify(laboratoryRepository).findAll();
    }

    @Test
    void update_ShouldUpdateLaboratory_WhenValidData() {
        // Given
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.of(laboratoryModel));
        when(laboratoryRepository.save(any(LaboratoryModel.class))).thenReturn(laboratoryModel);

        // When
        LaboratoryResponse result = laboratoryService.update(1L, laboratoryDto);

        // Then
        assertNotNull(result);
        verify(laboratoryRepository).findById(1L);
        verify(laboratoryRepository).save(laboratoryModel);
    }

    @Test
    void delete_ShouldDeleteLaboratory_WhenExists() {
        // Given
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.of(laboratoryModel));

        // When
        laboratoryService.delete(1L);

        // Then
        verify(laboratoryRepository).findById(1L);
        verify(laboratoryRepository).delete(laboratoryModel);
    }

    @Test
    void totalEvents_ShouldCalculateTotalEvents_WhenValidData() {
        // Given
        Long laboratoryId = 1L;
        Long categoryId = 1L;
        EventType eventType = EventType.MAINTENANCE;
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();

        EquipmentModel equipment = new EquipmentModel();
        equipment.setId(1L);

        EventModel event = new EventModel();
        event.setId(1L);
        event.setEquipment(equipment);

        EventTotalizerItemsResponse eventItem = new EventTotalizerItemsResponse();
        eventItem.setId(1L);

        EventTotalizerResponse eventTotalizer = new EventTotalizerResponse();
        eventTotalizer.setTotal(new BigDecimal("100.00"));
        eventTotalizer.setEventItems(Arrays.asList(eventItem));

        when(equipmentDao.findEquipment(laboratoryId, categoryId, null))
                .thenReturn(Arrays.asList(equipment));
        when(eventDao.findEventsByEquipments(anyList(), eq(eventType), eq(startDate), eq(endDate)))
                .thenReturn(Arrays.asList(event));
        when(eventService.totalEvents(eq(equipment), anyList()))
                .thenReturn(eventTotalizer);

        // When
        LaboratoryEventTotalizerResponse result = laboratoryService.totalEvents(
                laboratoryId, categoryId, eventType, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(new BigDecimal("100.00"), result.getTotal());
        assertFalse(result.getEquipmentTotalizer().isEmpty());

        verify(equipmentDao).findEquipment(laboratoryId, categoryId, null);
        verify(eventDao).findEventsByEquipments(anyList(), eq(eventType), eq(startDate), eq(endDate));
        verify(eventService).totalEvents(eq(equipment), anyList());
    }

    @Test
    void totalEvents_ShouldReturnZeroTotal_WhenNoEvents() {
        // Given
        Long laboratoryId = 1L;
        Long categoryId = 1L;
        EventType eventType = EventType.MAINTENANCE;
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();

        EquipmentModel equipment = new EquipmentModel();
        equipment.setId(1L);

        EventTotalizerResponse eventTotalizer = new EventTotalizerResponse();
        eventTotalizer.setTotal(BigDecimal.ZERO);
        eventTotalizer.setEventItems(Collections.emptyList());

        when(equipmentDao.findEquipment(laboratoryId, categoryId, null))
                .thenReturn(Arrays.asList(equipment));
        when(eventDao.findEventsByEquipments(anyList(), eq(eventType), eq(startDate), eq(endDate)))
                .thenReturn(Collections.emptyList());
        when(eventService.totalEvents(eq(equipment), anyList()))
                .thenReturn(eventTotalizer);

        // When
        LaboratoryEventTotalizerResponse result = laboratoryService.totalEvents(
                laboratoryId, categoryId, eventType, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getTotal());
        assertTrue(result.getEquipmentTotalizer().isEmpty());
    }
}