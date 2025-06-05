package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.BlockDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.BlockResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.BlockModel;
import br.com.ucs.laboratorio.gestao.domain.service.BlockService;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BlockControllerTest {

    @InjectMocks
    private BlockController blockController;

    @Mock
    private BlockService blockService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        BlockDto dto = new BlockDto();
        BlockResponse response = new BlockResponse();

        when(blockService.create(dto)).thenReturn(response);

        var result = blockController.create(dto);

        assertEquals(response, result.getBody());
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void testFindById() {
        Long id = 1L;
        BlockResponse response = new BlockResponse();

        try (MockedStatic<MapperUtil> mocked = mockStatic(MapperUtil.class)) {
            when(blockService.findById(id)).thenReturn(new BlockModel());
            mocked.when(() -> MapperUtil.mapObject(any(), eq(BlockResponse.class))).thenReturn(response);

            var result = blockController.findById(id);

            assertEquals(response, result.getBody());
        }
    }

    @Test
    void testFindAll() {
        List<BlockResponse> list = List.of(new BlockResponse());
        when(blockService.findAll()).thenReturn(list);

        var result = blockController.findAll();

        assertEquals(list, result.getBody());
    }

    @Test
    void testUpdate() {
        BlockDto dto = new BlockDto();
        BlockResponse response = new BlockResponse();

        when(blockService.update(1L, dto)).thenReturn(response);

        var result = blockController.update(1L, dto);

        assertEquals(response, result.getBody());
    }

    @Test
    void testDelete() {
        var result = blockController.delete(1L);

        assertEquals(204, result.getStatusCodeValue());
        verify(blockService).delete(1L);
    }
}
