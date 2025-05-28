package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.EquipmentDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EquipmentResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.EquipmentModel;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.EquipmentRepository;
import br.com.ucs.laboratorio.gestao.util.MapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private LaboratoryService laboratoryService;

    @Autowired
    private ModelMapper modelMapper;

    public EquipmentModel findById(Long id) {
        return equipmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Equipamento nao existe"));
    }

    public EquipmentResponse create(EquipmentDto equipmentDto){
        var laboratory = laboratoryService.findById(equipmentDto.getLaboratoryId());
        var equipmentModel = modelMapper.map(equipmentDto, EquipmentModel.class);
        equipmentModel.setLaboratory(laboratory);
        var save = equipmentRepository.save(equipmentModel);
        return modelMapper.map(save, EquipmentResponse.class);
    }

    public List<EquipmentResponse> findAll() {
        return MapperUtil.mapList(equipmentRepository.findAll(), EquipmentResponse.class);
    }
}
