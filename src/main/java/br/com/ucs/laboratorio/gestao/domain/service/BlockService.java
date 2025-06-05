package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.BlockDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.BlockResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.BlockModel;
import br.com.ucs.laboratorio.gestao.application.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.BlockRepository;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockService {

    @Autowired
    private BlockRepository blockRepository;

    public BlockModel findById(Long id) {
        return blockRepository.findById(id).orElseThrow(() -> new BusinessException("Bloco nao existe"));
    }

    public BlockResponse create(BlockDto blockDto){
        var blockModel = MapperUtil.mapObject(blockDto, BlockModel.class);
        var save = blockRepository.save(blockModel);
        return MapperUtil.mapObject(save, BlockResponse.class);
    }

    public List<BlockResponse> findAll() {
        return MapperUtil.mapList(blockRepository.findAll(), BlockResponse.class);
    }

    public BlockResponse update(Long id, BlockDto blockDto) {
        BlockModel block = findById(id);
        block.setDescription(blockDto.getDescription());
        BlockModel save = blockRepository.save(block);
        return MapperUtil.mapObject(save, BlockResponse.class);
    }

    public void delete(Long id) {
        blockRepository.delete(findById(id));
    }
}
