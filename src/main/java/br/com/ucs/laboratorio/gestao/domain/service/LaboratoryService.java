package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.LaboratoryDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventTotalizerResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.LaboratoryEventTotalizerResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.LaboratoryResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.LaboratoryModel;
import br.com.ucs.laboratorio.gestao.application.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.LaboratoryRepository;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LaboratoryService {

    @Autowired
    private LaboratoryRepository laboratoryRepository;

    @Autowired
    private BlockService blockService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ModelMapper modelMapper;

    public LaboratoryResponse create(LaboratoryDto laboratoryDto) {
        var block = blockService.findById(laboratoryDto.getBlockId());
        var laboratoryModel = modelMapper.map(laboratoryDto, LaboratoryModel.class);
        laboratoryModel.setBlock(block);
        var save = laboratoryRepository.save(laboratoryModel);
        return modelMapper.map(save, LaboratoryResponse.class);
    }

    public LaboratoryModel findById(Long id){
        return laboratoryRepository.findById(id).orElseThrow(() -> new BusinessException("Laboratorio nao existe"));
    }

    public List<LaboratoryResponse> findAll() {
        return MapperUtil.mapList(laboratoryRepository.findAll(), LaboratoryResponse.class);
    }

    public LaboratoryResponse update(Long id, LaboratoryDto laboratoryDto) {
        LaboratoryModel laboratory = findById(id);
        laboratory.setRoomNumber(laboratoryDto.getRoomNumber());
        laboratory.setRoomName(laboratoryDto.getRoomName());
        return MapperUtil.mapObject(laboratoryRepository.save(laboratory), LaboratoryResponse.class);
    }

    public void delete(Long id) {
        laboratoryRepository.delete(findById(id));
    }

    public LaboratoryEventTotalizerResponse totalEvents(Long id, LocalDate initialDate, LocalDate finalDate) {
        var laboratory = findById(id);
        var listEquipment = laboratory.getEquipments().stream()
                            .map(equip -> eventService.totalEvents(equip.getId(), initialDate, finalDate))
                            .toList();

        var total = listEquipment.stream()
                .map(EventTotalizerResponse::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new LaboratoryEventTotalizerResponse(total, listEquipment);

    }
}
