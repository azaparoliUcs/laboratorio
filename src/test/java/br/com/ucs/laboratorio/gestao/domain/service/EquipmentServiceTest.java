package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.EquipmentDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EquipmentResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.EquipmentModel;
import br.com.ucs.laboratorio.gestao.domain.entity.LaboratoryModel;
import br.com.ucs.laboratorio.gestao.domain.entity.TemplateModel;
import br.com.ucs.laboratorio.gestao.domain.type.PeriodCalibrationType;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.EquipmentRepository;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EquipmentServiceTest {

    @InjectMocks
    private EquipmentService equipmentService;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private LaboratoryService laboratoryService;

    @Mock
    private TemplateService templateService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_success() {
        Long id = 1L;
        EquipmentModel equipment = new EquipmentModel();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(equipment));

        EquipmentModel result = equipmentService.findById(id);

        assertEquals(equipment, result);
    }

    @Test
    void testFindById_notFound() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> equipmentService.findById(1L));
        assertEquals("Equipamento nao existe", ex.getMessage());
    }
//TODO AJUSTAR TESTE

//    @Test
//    void testCreate_success() {
//        EquipmentDto dto = new EquipmentDto();
//        dto.setLaboratoryId(2L);
//
//        LaboratoryModel lab = new LaboratoryModel();
//        EquipmentModel equipment = new EquipmentModel();
//        EquipmentModel saved = new EquipmentModel();
//        EquipmentResponse response = new EquipmentResponse();
//        var templateModelBuilder = TemplateModel.builder().periodCalibrationType(PeriodCalibrationType.FIVE_YEARS).build();
//
//        when(laboratoryService.findById(2L)).thenReturn(lab);
//        when(modelMapper.map(dto, EquipmentModel.class)).thenReturn(equipment);
//        when(equipmentRepository.save(equipment)).thenReturn(saved);
//        when(modelMapper.map(saved, EquipmentResponse.class)).thenReturn(response);
//        when(templateService.findById(any())).thenReturn(templateModelBuilder);
//
//        EquipmentResponse result = equipmentService.create(dto);
//
//        assertEquals(response, result);
//    }

    @Test
    void testFindAll_success() {
        List<EquipmentModel> models = List.of(new EquipmentModel(), new EquipmentModel());
        List<EquipmentResponse> responses = List.of(new EquipmentResponse(), new EquipmentResponse());

        when(equipmentRepository.findAll()).thenReturn(models);

        try (MockedStatic<MapperUtil> mocked = mockStatic(MapperUtil.class)) {
            mocked.when(() -> MapperUtil.mapList(models, EquipmentResponse.class)).thenReturn(responses);

            List<EquipmentResponse> result = equipmentService.findAll();

            assertEquals(2, result.size());
        }
    }

    @Test
    void testFindByLaboratoryId_success() {
        Long labId = 1L;
        LaboratoryModel lab = new LaboratoryModel();
        lab.setEquipments(List.of(new EquipmentModel(), new EquipmentModel()));
        List<EquipmentResponse> responses = List.of(new EquipmentResponse(), new EquipmentResponse());

        when(laboratoryService.findById(labId)).thenReturn(lab);

        try (MockedStatic<MapperUtil> mocked = mockStatic(MapperUtil.class)) {
            mocked.when(() -> MapperUtil.mapList(lab.getEquipments(), EquipmentResponse.class)).thenReturn(responses);

            List<EquipmentResponse> result = equipmentService.findByLaboratoryId(labId);

            assertEquals(2, result.size());
        }
    }

    @Test
    void testDelete_success() {
        Long id = 1L;
        EquipmentModel equipment = new EquipmentModel();

        when(equipmentRepository.findById(id)).thenReturn(Optional.of(equipment));

        equipmentService.delete(id);

        verify(equipmentRepository).delete(equipment);
    }

    @Test
    void testUpdate_success() {
        Long id = 1L;
        EquipmentDto dto = new EquipmentDto();
        dto.setLaboratoryId(2L);
        dto.setEquipmentTag("EQ123");
        dto.setPropertyNumber("PROP456");

        EquipmentModel existing = new EquipmentModel();
        LaboratoryModel lab = new LaboratoryModel();
        EquipmentModel saved = new EquipmentModel();
        EquipmentResponse response = new EquipmentResponse();

        when(equipmentRepository.findById(id)).thenReturn(Optional.of(existing));
        when(laboratoryService.findById(2L)).thenReturn(lab);
        when(equipmentRepository.save(existing)).thenReturn(saved);

        try (MockedStatic<MapperUtil> mocked = mockStatic(MapperUtil.class)) {
            mocked.when(() -> MapperUtil.mapObject(saved, EquipmentResponse.class)).thenReturn(response);

            EquipmentResponse result = equipmentService.update(id, dto);

            assertEquals(response, result);
            assertEquals("EQ123", existing.getEquipmentTag());
            assertEquals("PROP456", existing.getPropertyNumber());
            assertEquals(lab, existing.getLaboratory());
        }
    }

//    @Test
//    void testFindExpirationEquipment_whenDataIsInCache() {
//        Long labId = 1L;
//
//        EquipmentResponse equipment = new EquipmentResponse();
//        List<EquipmentResponse> cachedList = List.of(equipment);
//
//        EquipmentService.EXPIRATION_CACHE.put(labId, cachedList);
//
//        try (MockedStatic<MapperUtil> mocked = mockStatic(MapperUtil.class)) {
//            List<EquipmentResponse> responseList = List.of(new EquipmentResponse());
//
//            mocked.when(() -> MapperUtil.mapList(cachedList, EquipmentResponse.class))
//                    .thenReturn(responseList);
//
//            List<EquipmentResponse> result = equipmentService.findExpirationEquipment(labId);
//
//            assertEquals(responseList, result);
//            verify(equipmentRepository, never()).findExpiration(any(), any(), any());
//        }
//    }

}
