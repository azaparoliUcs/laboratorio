package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.LaboratoryDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.LaboratoryResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.LaboratoryModel;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.LaboratoryRepository;
import br.com.ucs.laboratorio.gestao.util.MapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaboratoryService {

    @Autowired
    private LaboratoryRepository laboratoryRepository;

    @Autowired
    private BlockService blockService;

    @Autowired
    private ModelMapper modelMapper;

    public LaboratoryModel findById(Long id){
        return laboratoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Laboratorio nao existe"));
    }

    public LaboratoryResponse create(LaboratoryDto laboratoryDto) {
        var block = blockService.findById(laboratoryDto.getBlockId());
        var laboratoryModel = modelMapper.map(laboratoryDto, LaboratoryModel.class);
        laboratoryModel.setBlockId(block.getId());
        var save = laboratoryRepository.save(laboratoryModel);
        return modelMapper.map(save, LaboratoryResponse.class);
    }

    public List<LaboratoryResponse> findAll() {
        return MapperUtil.mapList(laboratoryRepository.findAll(), LaboratoryResponse.class);
    }
}
