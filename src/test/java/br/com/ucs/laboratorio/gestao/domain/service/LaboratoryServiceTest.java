package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.LaboratoryDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.LaboratoryResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.BlockModel;
import br.com.ucs.laboratorio.gestao.domain.entity.LaboratoryModel;
import br.com.ucs.laboratorio.gestao.infrastructure.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.LaboratoryRepository;
import br.com.ucs.laboratorio.gestao.util.MapperUtil;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class LaboratoryServiceTest {

    @InjectMocks
    private LaboratoryService laboratoryService;

    @Mock
    private LaboratoryRepository laboratoryRepository;

    @Mock
    private BlockService blockService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_success() {
        Long id = 1L;
        LaboratoryModel model = new LaboratoryModel();

        when(laboratoryRepository.findById(id)).thenReturn(Optional.of(model));

        LaboratoryModel result = laboratoryService.findById(id);

        assertEquals(model, result);
    }

    @Test
    void testFindById_notFound() {
        when(laboratoryRepository.findById(any())).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> laboratoryService.findById(99L));
        assertEquals("Laboratorio nao existe", ex.getMessage());
    }

    @Test
    void testCreate_success() {
        LaboratoryDto dto = new LaboratoryDto();
        dto.setBlockId(10L);

        BlockModel block = new BlockModel();
        block.setId(10L);

        LaboratoryModel mapped = new LaboratoryModel();
        LaboratoryModel saved = new LaboratoryModel();
        LaboratoryResponse response = new LaboratoryResponse();

        when(blockService.findById(10L)).thenReturn(block);
        when(modelMapper.map(dto, LaboratoryModel.class)).thenReturn(mapped);
        when(laboratoryRepository.save(mapped)).thenReturn(saved);
        when(modelMapper.map(saved, LaboratoryResponse.class)).thenReturn(response);

        LaboratoryResponse result = laboratoryService.create(dto);

        assertEquals(response, result);
        assertEquals(10L, mapped.getBlockId());
    }

    @Test
    void testFindAll_success() {
        List<LaboratoryModel> models = List.of(new LaboratoryModel(), new LaboratoryModel());
        List<LaboratoryResponse> responses = List.of(new LaboratoryResponse(), new LaboratoryResponse());

        when(laboratoryRepository.findAll()).thenReturn(models);

        try (MockedStatic<MapperUtil> mocked = mockStatic(MapperUtil.class)) {
            mocked.when(() -> MapperUtil.mapList(models, LaboratoryResponse.class)).thenReturn(responses);

            List<LaboratoryResponse> result = laboratoryService.findAll();

            assertEquals(2, result.size());
        }
    }

    @Test
    void testUpdate_success() {
        Long id = 1L;
        LaboratoryDto dto = new LaboratoryDto();
        dto.setRoom("Sala 204");

        LaboratoryModel lab = new LaboratoryModel();
        LaboratoryModel saved = new LaboratoryModel();
        LaboratoryResponse response = new LaboratoryResponse();

        when(laboratoryRepository.findById(id)).thenReturn(Optional.of(lab));
        when(laboratoryRepository.save(lab)).thenReturn(saved);

        try (MockedStatic<MapperUtil> mocked = mockStatic(MapperUtil.class)) {
            mocked.when(() -> MapperUtil.mapObject(saved, LaboratoryResponse.class)).thenReturn(response);

            LaboratoryResponse result = laboratoryService.update(id, dto);

            assertEquals(response, result);
            assertEquals("Sala 204", lab.getRoom());
        }
    }

    @Test
    void testDelete_success() {
        Long id = 1L;
        LaboratoryModel lab = new LaboratoryModel();

        when(laboratoryRepository.findById(id)).thenReturn(Optional.of(lab));

        laboratoryService.delete(id);

        verify(laboratoryRepository).delete(lab);
    }
}
