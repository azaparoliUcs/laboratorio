package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.EventDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EquipmentTotalizerResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventTotalizerItemsResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventTotalizerResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.EquipmentModel;
import br.com.ucs.laboratorio.gestao.domain.entity.EventModel;
import br.com.ucs.laboratorio.gestao.domain.entity.TemplateModel;
import br.com.ucs.laboratorio.gestao.application.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.domain.type.EventStatusType;
import br.com.ucs.laboratorio.gestao.domain.type.EventType;
import br.com.ucs.laboratorio.gestao.domain.type.PeriodCalibrationType;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.EventRepository;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EquipmentService equipmentService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EventService eventService;

    private EventDto eventDto;
    private EventModel eventModel;
    private EventResponse eventResponse;
    private EquipmentModel equipment;
    private TemplateModel template;

    @BeforeEach
    void setUp() {
        // Setup Template
        template = new TemplateModel();
        template.setPeriodCalibrationType(PeriodCalibrationType.FIVE_YEARS);

        // Setup Equipment
        equipment = new EquipmentModel();
        equipment.setId(1L);
        equipment.setTemplate(template);
        equipment.setCalibrationFlag(true);

        // Setup EventDto
        eventDto = new EventDto();
        eventDto.setEquipmentId(1L);
        eventDto.setEventType(EventType.MAINTENANCE);
        eventDto.setStatus(EventStatusType.REGISTERED);
        eventDto.setCostValue(BigDecimal.valueOf(100.00));
        eventDto.setObservation("Test observation");
        eventDto.setFinalizedDate(LocalDate.now());

        // Setup EventModel
        eventModel = new EventModel();
        eventModel.setId(1L);
        eventModel.setEquipment(equipment);
        eventModel.setEventType(EventType.MAINTENANCE);
        eventModel.setStatus(EventStatusType.REGISTERED);
        eventModel.setCostValue(BigDecimal.valueOf(100.00));

        // Setup EventResponse
        eventResponse = new EventResponse();
        eventResponse.setId(1L);
        eventResponse.setEventType(EventType.MAINTENANCE);
        eventResponse.setStatus(EventStatusType.REGISTERED);
    }

    @Test
    void create_shouldCreateMaintenanceEventWithCalibrationRequested_whenEquipmentHasPeriodCalibration() {
        // Given
        when(equipmentService.findById(1L)).thenReturn(equipment);
        when(modelMapper.map(eventDto, EventModel.class)).thenReturn(eventModel);
        when(eventRepository.save(any(EventModel.class))).thenReturn(eventModel);
        when(modelMapper.map(eventModel, EventResponse.class)).thenReturn(eventResponse);

        // When
        EventResponse result = eventService.create(eventDto);

        // Then
        verify(equipmentService).findById(1L);
        verify(eventRepository).save(argThat(event ->
                event.getCalibrationRequested() &&
                        event.getEquipment().equals(equipment)
        ));
        assertEquals(eventResponse, result);
    }

    @Test
    void create_shouldNotSetCalibrationRequested_whenEquipmentHasNoPeriodCalibration() {
        // Given
        template.setPeriodCalibrationType(PeriodCalibrationType.NONE);
        when(equipmentService.findById(1L)).thenReturn(equipment);
        when(modelMapper.map(eventDto, EventModel.class)).thenReturn(eventModel);
        when(eventRepository.save(any(EventModel.class))).thenReturn(eventModel);
        when(modelMapper.map(eventModel, EventResponse.class)).thenReturn(eventResponse);

        // When
        EventResponse result = eventService.create(eventDto);

        // Then
        assertEquals(eventResponse, result);
    }

    @Test
    void create_shouldUpdateEquipmentCalibrationFlag_whenEventTypeIsCalibration() {
        // Given
        eventDto.setEventType(EventType.CALIBRATION);
        eventModel.setEventType(EventType.CALIBRATION);

        when(equipmentService.findById(1L)).thenReturn(equipment);
        when(modelMapper.map(eventDto, EventModel.class)).thenReturn(eventModel);
        when(eventRepository.save(any(EventModel.class))).thenReturn(eventModel);
        when(modelMapper.map(eventModel, EventResponse.class)).thenReturn(eventResponse);

        // When
        EventResponse result = eventService.create(eventDto);

        // Then
        verify(equipmentService).updateEquipment(argThat(eq -> !eq.getCalibrationFlag()));
        assertEquals(eventResponse, result);
    }

    @Test
    void findById_shouldReturnEventModel_whenEventExists() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.of(eventModel));

        // When
        EventModel result = eventService.findById(1L);

        // Then
        assertEquals(eventModel, result);
        verify(eventRepository).findById(1L);
    }

    @Test
    void findById_shouldThrowBusinessException_whenEventNotExists() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> eventService.findById(1L)
        );
        assertEquals("Evento nao existe", exception.getMessage());
    }

    @Test
    void findAll_shouldReturnAllEvents() {
        try (MockedStatic<MapperUtil> mapperUtil = mockStatic(MapperUtil.class)) {
            // Given
            List<EventModel> events = Arrays.asList(eventModel);
            List<EventResponse> expectedResponses = Arrays.asList(eventResponse);

            when(eventRepository.findAll()).thenReturn(events);
            mapperUtil.when(() -> MapperUtil.mapList(events, EventResponse.class))
                    .thenReturn(expectedResponses);

            // When
            List<EventResponse> result = eventService.findAll();

            // Then
            assertEquals(expectedResponses, result);
            verify(eventRepository).findAll();
        }
    }

    @Test
    void delete_shouldDeleteEvent_whenEventExists() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.of(eventModel));

        // When
        eventService.delete(1L);

        // Then
        verify(eventRepository).delete(eventModel);
    }

    @Test
    void update_shouldUpdateCertificateNumber_whenEventTypeIsCalibrationAndCertificateNumberProvided() {
        try (MockedStatic<MapperUtil> mapperUtil = mockStatic(MapperUtil.class)) {
            // Given
            String certificateNumber = "CERT-123";
            eventDto.setEventType(EventType.CALIBRATION);
            eventDto.setCertificateNumber(certificateNumber);

            when(eventRepository.findById(1L)).thenReturn(Optional.of(eventModel));
            when(eventRepository.save(any(EventModel.class))).thenReturn(eventModel);
            mapperUtil.when(() -> MapperUtil.mapObject(eventModel, EventResponse.class))
                    .thenReturn(eventResponse);

            // When
            EventResponse result = eventService.update(1L, eventDto);

            // Then
            verify(eventRepository).save(argThat(event ->
                    certificateNumber.equals(event.getCertificateNumber())
            ));
            assertEquals(eventResponse, result);
        }
    }

    @Test
    void update_shouldFinalizeEventAndUpdateEquipmentStatus_whenStatusIsFinalized() {
        try (MockedStatic<MapperUtil> mapperUtil = mockStatic(MapperUtil.class)) {
            // Given
            eventDto.setStatus(EventStatusType.FINALIZED);

            when(eventRepository.findById(1L)).thenReturn(Optional.of(eventModel));
            when(eventRepository.save(any(EventModel.class))).thenReturn(eventModel);
            mapperUtil.when(() -> MapperUtil.mapObject(eventModel, EventResponse.class))
                    .thenReturn(eventResponse);

            // When
            EventResponse result = eventService.update(1L, eventDto);

            // Then
            verify(equipmentService).updateStatus(eventDto.getEquipmentId(), eventModel);
            verify(eventRepository).save(argThat(event ->
                    EventStatusType.FINALIZED.equals(event.getStatus())
            ));
            assertEquals(eventResponse, result);
        }
    }

    @Test
    void update_shouldUpdateAllFields() {
        try (MockedStatic<MapperUtil> mapperUtil = mockStatic(MapperUtil.class)) {
            // Given
            LocalDate finalizedDate = LocalDate.now();
            BigDecimal costValue = BigDecimal.valueOf(200.00);
            String observation = "Updated observation";

            eventDto.setFinalizedDate(finalizedDate);
            eventDto.setCostValue(costValue);
            eventDto.setObservation(observation);

            when(eventRepository.findById(1L)).thenReturn(Optional.of(eventModel));
            when(eventRepository.save(any(EventModel.class))).thenReturn(eventModel);
            mapperUtil.when(() -> MapperUtil.mapObject(eventModel, EventResponse.class))
                    .thenReturn(eventResponse);

            // When
            EventResponse result = eventService.update(1L, eventDto);

            // Then
            verify(eventRepository).save(argThat(event ->
                    finalizedDate.equals(event.getFinalizedDate()) &&
                            costValue.equals(event.getCostValue()) &&
                            observation.equals(event.getObservation())
            ));
            assertEquals(eventResponse, result);
        }
    }

    @Test
    void totalEvents_shouldCalculateTotalAndReturnTotalizerResponse() {
        try (MockedStatic<MapperUtil> mapperUtil = mockStatic(MapperUtil.class)) {
            // Given
            EventTotalizerItemsResponse item1 = new EventTotalizerItemsResponse();
            item1.setCostValue(BigDecimal.valueOf(100.00));

            EventTotalizerItemsResponse item2 = new EventTotalizerItemsResponse();
            item2.setCostValue(BigDecimal.valueOf(50.00));

            List<EventTotalizerItemsResponse> events = Arrays.asList(item1, item2);
            EquipmentTotalizerResponse equipmentResponse = new EquipmentTotalizerResponse();

            mapperUtil.when(() -> MapperUtil.mapObject(equipment, EquipmentTotalizerResponse.class))
                    .thenReturn(equipmentResponse);

            // When
            EventTotalizerResponse result = eventService.totalEvents(equipment, events);

            // Then
            assertEquals(BigDecimal.valueOf(150.00), result.getTotal());
            assertEquals(equipmentResponse, result.getEquipment());
            assertEquals(events, result.getEventItems());
        }
    }

    @Test
    void totalEvents_shouldReturnZeroTotal_whenNoEvents() {
        try (MockedStatic<MapperUtil> mapperUtil = mockStatic(MapperUtil.class)) {
            // Given
            List<EventTotalizerItemsResponse> events = Arrays.asList();
            EquipmentTotalizerResponse equipmentResponse = new EquipmentTotalizerResponse();

            mapperUtil.when(() -> MapperUtil.mapObject(equipment, EquipmentTotalizerResponse.class))
                    .thenReturn(equipmentResponse);

            // When
            EventTotalizerResponse result = eventService.totalEvents(equipment, events);

            // Then
            assertEquals(BigDecimal.ZERO, result.getTotal());
            assertEquals(equipmentResponse, result.getEquipment());
            assertEquals(events, result.getEventItems());
        }
    }

    @Test
    void findByEquipmentId_shouldReturnEventsByEquipmentId() {
        try (MockedStatic<MapperUtil> mapperUtil = mockStatic(MapperUtil.class)) {
            // Given
            List<EventModel> events = Arrays.asList(eventModel);
            List<EventResponse> expectedResponses = Arrays.asList(eventResponse);

            when(eventRepository.findAllByEquipmentId(1L)).thenReturn(events);
            mapperUtil.when(() -> MapperUtil.mapList(events, EventResponse.class))
                    .thenReturn(expectedResponses);

            // When
            List<EventResponse> result = eventService.findByEquipmentId(1L);

            // Then
            assertEquals(expectedResponses, result);
            verify(eventRepository).findAllByEquipmentId(1L);
        }
    }

    @Test
    void create_shouldNotSetCalibrationRequested_whenEventTypeIsNotMaintenance() {
        // Given
        eventDto.setEventType(EventType.CALIBRATION);
        eventModel.setEventType(EventType.CALIBRATION);

        when(equipmentService.findById(1L)).thenReturn(equipment);
        when(modelMapper.map(eventDto, EventModel.class)).thenReturn(eventModel);
        when(eventRepository.save(any(EventModel.class))).thenReturn(eventModel);
        when(modelMapper.map(eventModel, EventResponse.class)).thenReturn(eventResponse);

        // When
        EventResponse result = eventService.create(eventDto);

        // Then
        assertEquals(eventResponse, result);
    }

    @Test
    void update_shouldNotUpdateCertificateNumber_whenCertificateNumberIsNull() {
        try (MockedStatic<MapperUtil> mapperUtil = mockStatic(MapperUtil.class)) {
            // Given
            eventDto.setEventType(EventType.CALIBRATION);
            eventDto.setCertificateNumber(null);
            String originalCertificate = "ORIGINAL-CERT";
            eventModel.setCertificateNumber(originalCertificate);

            when(eventRepository.findById(1L)).thenReturn(Optional.of(eventModel));
            when(eventRepository.save(any(EventModel.class))).thenReturn(eventModel);
            mapperUtil.when(() -> MapperUtil.mapObject(eventModel, EventResponse.class))
                    .thenReturn(eventResponse);

            // When
            EventResponse result = eventService.update(1L, eventDto);

            // Then
            verify(eventRepository).save(argThat(event ->
                    originalCertificate.equals(event.getCertificateNumber())
            ));
            assertEquals(eventResponse, result);
        }
    }
}