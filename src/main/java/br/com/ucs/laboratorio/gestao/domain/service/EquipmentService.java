package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.application.util.DateUtil;
import br.com.ucs.laboratorio.gestao.domain.dto.EquipmentDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EquipmentResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventTotalizerResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.EquipmentModel;
import br.com.ucs.laboratorio.gestao.domain.entity.EventModel;
import br.com.ucs.laboratorio.gestao.domain.type.EquipmentStatusType;
import br.com.ucs.laboratorio.gestao.domain.type.EventType;
import br.com.ucs.laboratorio.gestao.domain.type.PeriodCalibrationType;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.EquipmentRepository;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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
    private TemplateService templateService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ModelMapper modelMapper;

    public static final Map<Long, List<EquipmentResponse>> EXPIRATION_CACHE = new ConcurrentHashMap<>();

    public EquipmentResponse create(EquipmentDto equipmentDto){
        var laboratory = laboratoryService.findById(equipmentDto.getLaboratoryId());
        var template = templateService.findById(equipmentDto.getTemplateId());
        var equipmentModel = modelMapper.map(equipmentDto, EquipmentModel.class);
        equipmentModel.setEquipmentStatusType(EquipmentStatusType.AVAILABLE);

        if (!template.getPeriodCalibrationType().equals(PeriodCalibrationType.NONE)){
            equipmentModel.setEquipmentStatusType(EquipmentStatusType.UNAVAILABLE);
        }

        equipmentModel.setLaboratory(laboratory);
        var save = equipmentRepository.save(equipmentModel);

        if (save.getEquipmentStatusType().equals(EquipmentStatusType.UNAVAILABLE))
            eventService.generateEventCalibration(save.getId());

        return modelMapper.map(save, EquipmentResponse.class);
    }

    public EquipmentModel findById(Long id) {
        return equipmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Equipamento nao existe"));
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
            return EXPIRATION_CACHE.get(id);
        }

        LocalDate finalDate = LocalDate.now().plusDays(30);
        var equipments = equipmentRepository.findExpiration(id, LocalDate.now(), finalDate);

        var equipmentResponses = MapperUtil.mapList(equipments, EquipmentResponse.class);

        equipmentResponses.forEach(equip -> equip.setCalibrationExpiring(isBetween(equip.getNextCalibrationDate(), LocalDate.now(), finalDate)));

        return EXPIRATION_CACHE.put(id, equipmentResponses);
    }

    public void updateStatus(Long id, EventModel eventModel){
        EquipmentModel equipment = findById(id);
        switch (eventModel.getEventType()){
            case CALIBRATION:
                equipment.setNextCalibrationDate(DateUtil.calculateNextPeriodCalibration(equipment.getTemplate().getPeriodCalibrationType()));
                equipment.setEquipmentStatusType(EquipmentStatusType.AVAILABLE);
                break;
            case MAINTENANCE:
                equipment.setNextMaintenanceDate(DateUtil.calculateNextPeriodMaintenance(equipment.getTemplate().getPeriodMaintenanceType()));
                if (!eventModel.getCalibrationRequested())
                    equipment.setEquipmentStatusType(EquipmentStatusType.AVAILABLE);
            default:
                equipment.setEquipmentStatusType(EquipmentStatusType.AVAILABLE);
        }
        equipmentRepository.save(equipment);
    }

    private boolean isBetween(LocalDate data, LocalDate initial, LocalDate fim) {
        return data != null && !data.isBefore(initial) && !data.isAfter(fim);
    }
}
