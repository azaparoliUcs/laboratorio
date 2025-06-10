package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.EquipmentDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EquipmentResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.EquipmentModel;
import br.com.ucs.laboratorio.gestao.domain.type.EquipmentStatusType;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.EquipmentRepository;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private LaboratoryService laboratoryService;

    @Autowired
    private ModelMapper modelMapper;

    public static final Map<Long, List<EquipmentModel>> EXPIRATION_CACHE = new ConcurrentHashMap<>();

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

    public List<EquipmentResponse> findByLaboratoryId(Long id) {
        var laboratory = laboratoryService.findById(id);
        return MapperUtil.mapList(laboratory.getEquipments(), EquipmentResponse.class);
    }

    public void delete(Long id) {
        equipmentRepository.delete(findById(id));
    }

    public EquipmentResponse update(Long id, EquipmentDto equipmentDto) {
        EquipmentModel equipment = findById(id);
        equipment.setLaboratory(laboratoryService.findById(equipmentDto.getLaboratoryId()));
        equipment.setEquipmentTag(equipmentDto.getEquipmentTag());
        equipment.setPropertyNumber(equipmentDto.getPropertyNumber());
        return MapperUtil.mapObject(equipmentRepository.save(equipment), EquipmentResponse.class);
    }

    public List<EquipmentResponse> findExpirationEquipment(Long id) {
        if(EXPIRATION_CACHE.containsKey(id)){
            return MapperUtil.mapList(EXPIRATION_CACHE.get(id), EquipmentResponse.class);
        }

        LocalDate finalDate = LocalDate.now().plusDays(30);
        var equipments = equipmentRepository.findExpiration(id, LocalDate.now(), finalDate);

        EXPIRATION_CACHE.put(id, equipments);

        return MapperUtil.mapList(equipments, EquipmentResponse.class);
    }

    public void updateStatus(Long id){
        EquipmentModel equipment = findById(id);
        equipment.setEquipmentStatusType(EquipmentStatusType.AVAILABLE);
        equipmentRepository.save(equipment);
    }
}
