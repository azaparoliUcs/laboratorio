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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlockServiceTest {

    @Mock
    private BlockRepository blockRepository;

    @InjectMocks
    private BlockService blockService;

    private BlockModel blockModel;
    private BlockDto blockDto;
    private BlockResponse blockResponse;

    @BeforeEach
    void setUp() {
        blockModel = new BlockModel();
        blockModel.setId(1L);
        blockModel.setDescription("Bloco A");

        blockDto = new BlockDto();
        blockDto.setDescription("Bloco A");

        blockResponse = new BlockResponse();
        blockResponse.setId(1L);
        blockResponse.setDescription("Bloco A");
    }

    @Test
    void findById_WhenBlockExists_ShouldReturnBlock() {
        // Given
        Long id = 1L;
        when(blockRepository.findById(id)).thenReturn(Optional.of(blockModel));

        // When
        BlockModel result = blockService.findById(id);

        // Then
        assertNotNull(result);
        assertEquals(blockModel.getId(), result.getId());
        assertEquals(blockModel.getDescription(), result.getDescription());
        verify(blockRepository).findById(id);
    }

    @Test
    void findById_WhenBlockDoesNotExist_ShouldThrowBusinessException() {
        // Given
        Long id = 1L;
        when(blockRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> blockService.findById(id));
        assertEquals("Bloco nao existe", exception.getMessage());
        verify(blockRepository).findById(id);
    }

    @Test
    void create_ShouldCreateBlockAndReturnResponse() {
        // Given
        when(blockRepository.save(any(BlockModel.class))).thenReturn(blockModel);

        try (MockedStatic<MapperUtil> mockedMapperUtil = mockStatic(MapperUtil.class)) {
            mockedMapperUtil.when(() -> MapperUtil.mapObject(blockDto, BlockModel.class))
                    .thenReturn(blockModel);
            mockedMapperUtil.when(() -> MapperUtil.mapObject(blockModel, BlockResponse.class))
                    .thenReturn(blockResponse);

            // When
            BlockResponse result = blockService.create(blockDto);

            // Then
            assertNotNull(result);
            assertEquals(blockResponse.getId(), result.getId());
            assertEquals(blockResponse.getDescription(), result.getDescription());
            verify(blockRepository).save(any(BlockModel.class));
        }
    }

    @Test
    void findAll_ShouldReturnListOfBlockResponses() {
        // Given
        List<BlockModel> blockList = Arrays.asList(blockModel);
        List<BlockResponse> expectedResponses = Arrays.asList(blockResponse);

        when(blockRepository.findAll()).thenReturn(blockList);

        try (MockedStatic<MapperUtil> mockedMapperUtil = mockStatic(MapperUtil.class)) {
            mockedMapperUtil.when(() -> MapperUtil.mapList(blockList, BlockResponse.class))
                    .thenReturn(expectedResponses);

            // When
            List<BlockResponse> result = blockService.findAll();

            // Then
            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(1, result.size());
            assertEquals(expectedResponses.get(0).getId(), result.get(0).getId());
            verify(blockRepository).findAll();
        }
    }

    @Test
    void update_WhenBlockExists_ShouldUpdateAndReturnResponse() {
        // Given
        Long id = 1L;
        String newDescription = "Bloco A Atualizado";
        blockDto.setDescription(newDescription);

        BlockModel updatedBlock = new BlockModel();
        updatedBlock.setId(id);
        updatedBlock.setDescription(newDescription);

        BlockResponse updatedResponse = new BlockResponse();
        updatedResponse.setId(id);
        updatedResponse.setDescription(newDescription);

        when(blockRepository.findById(id)).thenReturn(Optional.of(blockModel));
        when(blockRepository.save(any(BlockModel.class))).thenReturn(updatedBlock);

        try (MockedStatic<MapperUtil> mockedMapperUtil = mockStatic(MapperUtil.class)) {
            mockedMapperUtil.when(() -> MapperUtil.mapObject(updatedBlock, BlockResponse.class))
                    .thenReturn(updatedResponse);

            // When
            BlockResponse result = blockService.update(id, blockDto);

            // Then
            assertNotNull(result);
            assertEquals(updatedResponse.getId(), result.getId());
            assertEquals(newDescription, result.getDescription());
            verify(blockRepository).findById(id);
            verify(blockRepository).save(any(BlockModel.class));
        }
    }

    @Test
    void update_WhenBlockDoesNotExist_ShouldThrowBusinessException() {
        // Given
        Long id = 1L;
        when(blockRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> blockService.update(id, blockDto));
        assertEquals("Bloco nao existe", exception.getMessage());
        verify(blockRepository).findById(id);
        verify(blockRepository, never()).save(any(BlockModel.class));
    }

    @Test
    void delete_WhenBlockExists_ShouldDeleteBlock() {
        // Given
        Long id = 1L;
        when(blockRepository.findById(id)).thenReturn(Optional.of(blockModel));

        // When
        blockService.delete(id);

        // Then
        verify(blockRepository).findById(id);
        verify(blockRepository).delete(blockModel);
    }

    @Test
    void delete_WhenBlockDoesNotExist_ShouldThrowBusinessException() {
        // Given
        Long id = 1L;
        when(blockRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> blockService.delete(id));
        assertEquals("Bloco nao existe", exception.getMessage());
        verify(blockRepository).findById(id);
        verify(blockRepository, never()).delete(any(BlockModel.class));
    }
}