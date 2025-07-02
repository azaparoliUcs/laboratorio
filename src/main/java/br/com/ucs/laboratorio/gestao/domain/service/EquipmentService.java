package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.application.util.DateUtil;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import br.com.ucs.laboratorio.gestao.domain.dao.EquipmentDao;
import br.com.ucs.laboratorio.gestao.domain.dto.EquipmentDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EquipmentResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.EquipmentModel;
import br.com.ucs.laboratorio.gestao.domain.entity.EventModel;
import br.com.ucs.laboratorio.gestao.domain.type.EquipmentStatusType;
import br.com.ucs.laboratorio.gestao.domain.type.PeriodCalibrationType;
import br.com.ucs.laboratorio.gestao.domain.type.PeriodMaintenanceType;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.EquipmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private EquipmentDao equipmentDao;

    public EquipmentResponse create(EquipmentDto equipmentDto){
        var laboratory = laboratoryService.findById(equipmentDto.getLaboratoryId());
        var template = templateService.findById(equipmentDto.getTemplateId());
        var equipmentModel = modelMapper.map(equipmentDto, EquipmentModel.class);
        equipmentModel.setEquipmentStatusType(EquipmentStatusType.AVAILABLE);

        if (!template.getPeriodCalibrationType().equals(PeriodCalibrationType.NONE)){
            equipmentModel.setEquipmentStatusType(EquipmentStatusType.UNAVAILABLE);
            equipmentModel.setCalibrationFlag(true);
        }

        if (!template.getPeriodMaintenanceType().equals(PeriodMaintenanceType.NONE))
            equipmentModel.setNextMaintenanceDate(DateUtil.calculateNextPeriodMaintenance(template.getPeriodMaintenanceType()));

        equipmentModel.setLaboratory(laboratory);
        var save = equipmentRepository.save(equipmentModel);

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
        equipment.setCalibrationFlag(false);
        return MapperUtil.mapObject(equipmentRepository.save(equipment), EquipmentResponse.class);
    }

    public List<EquipmentResponse> findExpirationEquipment(Long id) {
        LocalDate finalDate = LocalDate.now().plusDays(30);

        List<EquipmentModel> equipments;
        if (Objects.nonNull(id)){
            equipments = equipmentRepository.findExpirationByLaboratoryId(id, LocalDate.now(), finalDate);
        }else {
            equipments = equipmentRepository.findExpiration(LocalDate.now(), finalDate);
        }

        var equipmentResponses = MapperUtil.mapList(equipments, EquipmentResponse.class);

        equipmentResponses.forEach(equip -> equip.setDaysExpiration(isBetween(equip.getNextCalibrationDate())));

        return equipmentResponses;
    }

    public void updateEquipment(EquipmentModel equipmentModel){
        equipmentRepository.save(equipmentModel);
    }

    public void updateStatus(Long id, EventModel eventModel){
        EquipmentModel equipment = findById(id);
        switch (eventModel.getEventType()){
            case CALIBRATION:
                equipment.setNextCalibrationDate(DateUtil.calculateNextPeriodCalibration(equipment.getTemplate().getPeriodCalibrationType()));
                equipment.setEquipmentStatusType(EquipmentStatusType.AVAILABLE);
                equipment.setCalibrationFlag(false);
                break;
            case MAINTENANCE:
                equipment.setNextMaintenanceDate(DateUtil.calculateNextPeriodMaintenance(equipment.getTemplate().getPeriodMaintenanceType()));
                if (!eventModel.getCalibrationRequested()){
                    equipment.setEquipmentStatusType(EquipmentStatusType.AVAILABLE);
                    equipment.setCalibrationFlag(false);
                }
            default:
                equipment.setEquipmentStatusType(EquipmentStatusType.AVAILABLE);
                equipment.setCalibrationFlag(false);
        }
        equipmentRepository.save(equipment);
    }

    private Long isBetween(LocalDate data) {
        var now = LocalDate.now();
        if (Objects.isNull(data) || now.isAfter(data)){
            return -1L;
        }else {
            return ChronoUnit.DAYS.between(now, data);
        }
    }

    public List<EquipmentResponse> filter(Long laboratoryId, Long categoryId, EquipmentStatusType status) {
        return MapperUtil.mapList(equipmentDao.findEquipment(laboratoryId, categoryId, status), EquipmentResponse.class);
    }
}
