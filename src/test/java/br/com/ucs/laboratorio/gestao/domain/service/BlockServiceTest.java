package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.BlockDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.BlockResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.BlockModel;
import br.com.ucs.laboratorio.gestao.application.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.BlockRepository;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class BlockServiceTest {

    @InjectMocks
    private BlockService blockService;

    @Mock
    private BlockRepository blockRepository;

    @Mock
    private BlockModel mockBlockModel;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_success() {
        Long id = 1L;
        BlockModel model = new BlockModel();
        when(blockRepository.findById(id)).thenReturn(Optional.of(model));

        BlockModel result = blockService.findById(id);

        assertEquals(model, result);
        verify(blockRepository).findById(id);
    }

    @Test
    void testFindById_notFound() {
        when(blockRepository.findById(any())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> blockService.findById(1L));
        assertEquals("Bloco nao existe", exception.getMessage());
    }

    @Test
    void testCreate_success() {
        BlockDto dto = new BlockDto();
        BlockModel model = new BlockModel();
        BlockModel savedModel = new BlockModel();
        BlockResponse response = new BlockResponse();

        try (MockedStatic<MapperUtil> mapperUtil = mockStatic(MapperUtil.class)) {
            mapperUtil.when(() -> MapperUtil.mapObject(dto, BlockModel.class)).thenReturn(model);
            when(blockRepository.save(model)).thenReturn(savedModel);
            mapperUtil.when(() -> MapperUtil.mapObject(savedModel, BlockResponse.class)).thenReturn(response);

            BlockResponse result = blockService.create(dto);

            assertEquals(response, result);
        }
    }

    @Test
    void testFindAll_success() {
        List<BlockModel> models = Arrays.asList(new BlockModel(), new BlockModel());
        List<BlockResponse> responses = Arrays.asList(new BlockResponse(), new BlockResponse());

        when(blockRepository.findAll()).thenReturn(models);

        try (MockedStatic<MapperUtil> mapperUtil = mockStatic(MapperUtil.class)) {
            mapperUtil.when(() -> MapperUtil.mapList(models, BlockResponse.class)).thenReturn(responses);

            List<BlockResponse> result = blockService.findAll();

            assertEquals(2, result.size());
        }
    }

    @Test
    void testUpdate_success() {
        Long id = 1L;
        BlockDto dto = new BlockDto();
        dto.setDescription("New Description");

        BlockModel model = new BlockModel();
        model.setDescription("Old Description");

        BlockModel savedModel = new BlockModel();
        savedModel.setDescription("New Description");

        BlockResponse response = new BlockResponse();

        when(blockRepository.findById(id)).thenReturn(Optional.of(model));
        when(blockRepository.save(model)).thenReturn(savedModel);

        try (MockedStatic<MapperUtil> mapperUtil = mockStatic(MapperUtil.class)) {
            mapperUtil.when(() -> MapperUtil.mapObject(savedModel, BlockResponse.class)).thenReturn(response);

            BlockResponse result = blockService.update(id, dto);

            assertEquals(response, result);
            assertEquals("New Description", model.getDescription());
        }
    }

    @Test
    void testDelete_success() {
        Long id = 1L;
        BlockModel model = new BlockModel();

        when(blockRepository.findById(id)).thenReturn(Optional.of(model));

        blockService.delete(id);

        verify(blockRepository).delete(model);
    }
}
