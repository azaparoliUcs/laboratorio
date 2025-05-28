package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.BlockDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.BlockResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.BlockModel;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.BlockRepository;
import br.com.ucs.laboratorio.gestao.util.MapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockService {

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private ModelMapper modelMapper;

    public BlockModel findById(Long id) {
        return blockRepository.findById(id).orElseThrow(() -> new RuntimeException("Bloco nao existe"));
    }

    public BlockResponse create(BlockDto blockDto){
        var blockModel = modelMapper.map(blockDto, BlockModel.class);
        var save = blockRepository.save(blockModel);
        return modelMapper.map(save, BlockResponse.class);
    }

    public List<BlockResponse> findAll() {
        return MapperUtil.mapList(blockRepository.findAll(), BlockResponse.class);
    }
}
