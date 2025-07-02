package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.BlockDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.BlockResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.BlockModel;
import br.com.ucs.laboratorio.gestao.domain.service.BlockService;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BlockControllerTest {

    @Mock
    private BlockService blockService;

    @InjectMocks
    private BlockController blockController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private BlockDto blockDto;
    private BlockResponse blockResponse;
    private BlockModel block;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(blockController).build();
        objectMapper = new ObjectMapper();

        blockDto = new BlockDto();
        blockDto.setDescription("Bloco A");
        blockDto.setDescription("Bloco de laboratórios A");

        blockResponse = new BlockResponse();
        blockResponse.setId(1L);
        blockResponse.setDescription("Bloco de laboratórios A");

        block = new BlockModel();
        block.setId(1L);
        block.setDescription("Bloco A");
        block.setDescription("Bloco de laboratórios A");
    }

    @Test
    void shouldCreateBlockSuccessfully() throws Exception {
        when(blockService.create(any(BlockDto.class))).thenReturn(blockResponse);

        mockMvc.perform(post("/block")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blockDto)))
                .andExpect(status().isOk());

        verify(blockService).create(any(BlockDto.class));
    }

    @Test
    void shouldFindBlockByIdSuccessfully() throws Exception {
        try (MockedStatic<MapperUtil> mapperUtil = mockStatic(MapperUtil.class)) {
            when(blockService.findById(1L)).thenReturn(block);
            mapperUtil.when(() -> MapperUtil.mapObject(block, BlockResponse.class))
                    .thenReturn(blockResponse);

            mockMvc.perform(get("/block/1"))
                    .andExpect(status().isOk());

            verify(blockService).findById(1L);
        }
    }

    @Test
    void shouldFindAllBlocksSuccessfully() throws Exception {
        List<BlockResponse> blocks = Arrays.asList(blockResponse);
        when(blockService.findAll()).thenReturn(blocks);

        mockMvc.perform(get("/block"))
                .andExpect(status().isOk());

        verify(blockService).findAll();
    }

    @Test
    void shouldUpdateBlockSuccessfully() throws Exception {
        when(blockService.update(eq(1L), any(BlockDto.class))).thenReturn(blockResponse);

        mockMvc.perform(put("/block/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blockDto)))
                .andExpect(status().isOk());

        verify(blockService).update(eq(1L), any(BlockDto.class));
    }

    @Test
    void shouldDeleteBlockSuccessfully() throws Exception {
        mockMvc.perform(delete("/block/1"))
                .andExpect(status().isNoContent());

        verify(blockService).delete(1L);
    }
}