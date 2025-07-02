package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.application.util.DateUtil;
import br.com.ucs.laboratorio.gestao.domain.dao.EquipmentDao;
import br.com.ucs.laboratorio.gestao.domain.dto.EquipmentDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EquipmentResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.EquipmentModel;
import br.com.ucs.laboratorio.gestao.domain.entity.EventModel;
import br.com.ucs.laboratorio.gestao.domain.entity.LaboratoryModel;
import br.com.ucs.laboratorio.gestao.domain.entity.TemplateModel;
import br.com.ucs.laboratorio.gestao.domain.type.EquipmentStatusType;
import br.com.ucs.laboratorio.gestao.domain.type.EventType;
import br.com.ucs.laboratorio.gestao.domain.type.PeriodCalibrationType;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.EquipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private LaboratoryService laboratoryService;

    @Mock
    private TemplateService templateService;

    @Mock
    private EventService eventService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EquipmentDao equipmentDao;

    @InjectMocks
    private EquipmentService equipmentService;

    private EquipmentDto equipmentDto;
    private EquipmentModel equipmentModel;
    private EquipmentResponse equipmentResponse;
    private LaboratoryModel laboratoryModel;
    private TemplateModel templateModel;
    private EventModel eventModel;

    @BeforeEach
    void setUp() {
        equipmentDto = new EquipmentDto();
        equipmentDto.setLaboratoryId(1L);
        equipmentDto.setTemplateId(1L);
        equipmentDto.setEquipmentTag("EQ001");
        equipmentDto.setPropertyNumber("PROP001");

        laboratoryModel = new LaboratoryModel();
        laboratoryModel.setId(1L);

        templateModel = new TemplateModel();
        templateModel.setId(1L);
        templateModel.setPeriodCalibrationType(PeriodCalibrationType.NONE);

        equipmentModel = new EquipmentModel();
        equipmentModel.setId(1L);
        equipmentModel.setEquipmentTag("EQ001");
        equipmentModel.setPropertyNumber("PROP001");
        equipmentModel.setLaboratory(laboratoryModel);
        equipmentModel.setTemplate(templateModel);

        equipmentResponse = new EquipmentResponse();
        equipmentResponse.setId(1L);
        equipmentResponse.setEquipmentTag("EQ001");

        eventModel = new EventModel();
        eventModel.setEventType(EventType.CALIBRATION);
        eventModel.setCalibrationRequested(false);
    }

    @Test
    void create_WithNoneCalibrationType_ShouldCreateAvailableEquipment() {
        // Arrange
        when(laboratoryService.findById(1L)).thenReturn(laboratoryModel);
        when(templateService.findById(1L)).thenReturn(templateModel);
        when(modelMapper.map(equipmentDto, EquipmentModel.class)).thenReturn(equipmentModel);
        when(equipmentRepository.save(any(EquipmentModel.class))).thenReturn(equipmentModel);
        when(modelMapper.map(equipmentModel, EquipmentResponse.class)).thenReturn(equipmentResponse);

        // Act
        EquipmentResponse result = equipmentService.create(equipmentDto);

        // Assert
        assertNotNull(result);
        assertEquals(equipmentResponse.getId(), result.getId());
        verify(equipmentRepository).save(argThat(eq ->
                eq.getEquipmentStatusType() == EquipmentStatusType.AVAILABLE &&
                        !eq.getCalibrationFlag()
        ));
    }

    @Test
    void create_WithCalibrationRequired_ShouldCreateUnavailableEquipment() {
        // Arrange
        templateModel.setPeriodCalibrationType(PeriodCalibrationType.FIVE_YEARS);
        when(laboratoryService.findById(1L)).thenReturn(laboratoryModel);
        when(templateService.findById(1L)).thenReturn(templateModel);
        when(modelMapper.map(equipmentDto, EquipmentModel.class)).thenReturn(equipmentModel);
        when(equipmentRepository.save(any(EquipmentModel.class))).thenReturn(equipmentModel);
        when(modelMapper.map(equipmentModel, EquipmentResponse.class)).thenReturn(equipmentResponse);

        // Act
        EquipmentResponse result = equipmentService.create(equipmentDto);

        // Assert
        assertNotNull(result);
        verify(equipmentRepository).save(argThat(eq ->
                eq.getEquipmentStatusType() == EquipmentStatusType.UNAVAILABLE &&
                        eq.getCalibrationFlag()
        ));
    }

    @Test
    void findById_WithValidId_ShouldReturnEquipment() {
        // Arrange
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipmentModel));

        // Act
        EquipmentModel result = equipmentService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(equipmentModel.getId(), result.getId());
    }

    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> equipmentService.findById(1L));
        assertEquals("Equipamento nao existe", exception.getMessage());
    }

    @Test
    void findAll_ShouldReturnAllEquipments() {
        // Arrange
        List<EquipmentModel> equipments = Arrays.asList(equipmentModel);
        when(equipmentRepository.findAll()).thenReturn(equipments);

        try (MockedStatic<br.com.ucs.laboratorio.gestao.application.util.MapperUtil> mapperUtil =
                     mockStatic(br.com.ucs.laboratorio.gestao.application.util.MapperUtil.class)) {

            mapperUtil.when(() -> br.com.ucs.laboratorio.gestao.application.util.MapperUtil
                            .mapList(equipments, EquipmentResponse.class))
                    .thenReturn(Arrays.asList(equipmentResponse));

            // Act
            List<EquipmentResponse> result = equipmentService.findAll();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(equipmentResponse.getId(), result.get(0).getId());
        }
    }

    @Test
    void findByLaboratoryId_ShouldReturnEquipmentsByLaboratory() {
        // Arrange
        laboratoryModel.setEquipments(Arrays.asList(equipmentModel));
        when(laboratoryService.findById(1L)).thenReturn(laboratoryModel);

        try (MockedStatic<br.com.ucs.laboratorio.gestao.application.util.MapperUtil> mapperUtil =
                     mockStatic(br.com.ucs.laboratorio.gestao.application.util.MapperUtil.class)) {

            mapperUtil.when(() -> br.com.ucs.laboratorio.gestao.application.util.MapperUtil
                            .mapList(laboratoryModel.getEquipments(), EquipmentResponse.class))
                    .thenReturn(Arrays.asList(equipmentResponse));

            // Act
            List<EquipmentResponse> result = equipmentService.findByLaboratoryId(1L);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Test
    void delete_WithValidId_ShouldDeleteEquipment() {
        // Arrange
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipmentModel));

        // Act
        equipmentService.delete(1L);

        // Assert
        verify(equipmentRepository).delete(equipmentModel);
    }

    @Test
    void update_WithValidData_ShouldUpdateEquipment() {
        // Arrange
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipmentModel));
        when(laboratoryService.findById(1L)).thenReturn(laboratoryModel);
        when(equipmentRepository.save(any(EquipmentModel.class))).thenReturn(equipmentModel);

        try (MockedStatic<br.com.ucs.laboratorio.gestao.application.util.MapperUtil> mapperUtil =
                     mockStatic(br.com.ucs.laboratorio.gestao.application.util.MapperUtil.class)) {

            mapperUtil.when(() -> br.com.ucs.laboratorio.gestao.application.util.MapperUtil
                            .mapObject(equipmentModel, EquipmentResponse.class))
                    .thenReturn(equipmentResponse);

            // Act
            EquipmentResponse result = equipmentService.update(1L, equipmentDto);

            // Assert
            assertNotNull(result);
            verify(equipmentRepository).save(argThat(eq -> !eq.getCalibrationFlag()));
        }
    }

    @Test
    void findExpirationEquipment_WithLaboratoryId_ShouldReturnExpiringEquipments() {
        // Arrange
        LocalDate now = LocalDate.now();
        LocalDate finalDate = now.plusDays(30);
        equipmentModel.setNextCalibrationDate(now.plusDays(15));

        when(equipmentRepository.findExpirationByLaboratoryId(1L, now, finalDate))
                .thenReturn(Arrays.asList(equipmentModel));

        try (MockedStatic<br.com.ucs.laboratorio.gestao.application.util.MapperUtil> mapperUtil =
                     mockStatic(br.com.ucs.laboratorio.gestao.application.util.MapperUtil.class)) {

            equipmentResponse.setNextCalibrationDate(now.plusDays(15));
            mapperUtil.when(() -> br.com.ucs.laboratorio.gestao.application.util.MapperUtil
                            .mapList(Arrays.asList(equipmentModel), EquipmentResponse.class))
                    .thenReturn(Arrays.asList(equipmentResponse));

            // Act
            List<EquipmentResponse> result = equipmentService.findExpirationEquipment(1L);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(15L, result.get(0).getDaysExpiration());
        }
    }

    @Test
    void findExpirationEquipment_WithoutLaboratoryId_ShouldReturnAllExpiringEquipments() {
        // Arrange
        LocalDate now = LocalDate.now();
        LocalDate finalDate = now.plusDays(30);

        when(equipmentRepository.findExpiration(now, finalDate))
                .thenReturn(Arrays.asList(equipmentModel));

        try (MockedStatic<br.com.ucs.laboratorio.gestao.application.util.MapperUtil> mapperUtil =
                     mockStatic(br.com.ucs.laboratorio.gestao.application.util.MapperUtil.class)) {

            mapperUtil.when(() -> br.com.ucs.laboratorio.gestao.application.util.MapperUtil
                            .mapList(Arrays.asList(equipmentModel), EquipmentResponse.class))
                    .thenReturn(Arrays.asList(equipmentResponse));

            // Act
            List<EquipmentResponse> result = equipmentService.findExpirationEquipment(null);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Test
    void updateStatus_WithCalibrationEvent_ShouldUpdateEquipmentStatus() {
        // Arrange
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipmentModel));

        try (MockedStatic<br.com.ucs.laboratorio.gestao.application.util.DateUtil> dateUtil =
                     mockStatic(br.com.ucs.laboratorio.gestao.application.util.DateUtil.class)) {

            LocalDate nextCalibration = LocalDate.now().plusMonths(1);
            dateUtil.when(() -> DateUtil.calculateNextPeriodCalibration(any()))
                    .thenReturn(nextCalibration);

            // Act
            equipmentService.updateStatus(1L, eventModel);

            // Assert
            verify(equipmentRepository).save(argThat(eq ->
                    eq.getEquipmentStatusType() == EquipmentStatusType.AVAILABLE &&
                            !eq.getCalibrationFlag() &&
                            eq.getNextCalibrationDate().equals(nextCalibration)
            ));
        }
    }

    @Test
    void updateStatus_WithMaintenanceEvent_ShouldUpdateEquipmentStatus() {
        // Arrange
        eventModel.setEventType(EventType.MAINTENANCE);
        eventModel.setCalibrationRequested(false);
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipmentModel));

        try (MockedStatic<br.com.ucs.laboratorio.gestao.application.util.DateUtil> dateUtil =
                     mockStatic(br.com.ucs.laboratorio.gestao.application.util.DateUtil.class)) {

            LocalDate nextMaintenance = LocalDate.now().plusMonths(3);
            dateUtil.when(() -> DateUtil.calculateNextPeriodMaintenance(any()))
                    .thenReturn(nextMaintenance);

            // Act
            equipmentService.updateStatus(1L, eventModel);

            // Assert
            verify(equipmentRepository).save(argThat(eq ->
                    eq.getEquipmentStatusType() == EquipmentStatusType.AVAILABLE &&
                            !eq.getCalibrationFlag() &&
                            eq.getNextMaintenanceDate().equals(nextMaintenance)
            ));
        }
    }

    @Test
    void filter_ShouldReturnFilteredEquipments() {
        // Arrange
        when(equipmentDao.findEquipment(1L, 1L, EquipmentStatusType.AVAILABLE))
                .thenReturn(Arrays.asList(equipmentModel));

        try (MockedStatic<br.com.ucs.laboratorio.gestao.application.util.MapperUtil> mapperUtil =
                     mockStatic(br.com.ucs.laboratorio.gestao.application.util.MapperUtil.class)) {

            mapperUtil.when(() -> br.com.ucs.laboratorio.gestao.application.util.MapperUtil
                            .mapList(Arrays.asList(equipmentModel), EquipmentResponse.class))
                    .thenReturn(Arrays.asList(equipmentResponse));

            // Act
            List<EquipmentResponse> result = equipmentService.filter(1L, 1L, EquipmentStatusType.AVAILABLE);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Test
    void updateEquipment_ShouldSaveEquipment() {
        // Arrange
        when(equipmentRepository.save(equipmentModel)).thenReturn(equipmentModel);

        // Act
        equipmentService.updateEquipment(equipmentModel);

        // Assert
        verify(equipmentRepository).save(equipmentModel);
    }
}