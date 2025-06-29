package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dao.EquipmentDao;
import br.com.ucs.laboratorio.gestao.domain.dao.EventDao;
import br.com.ucs.laboratorio.gestao.domain.dto.EventDto;
import br.com.ucs.laboratorio.gestao.domain.dto.LaboratoryDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventTotalizerItemsResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventTotalizerResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.LaboratoryEventTotalizerResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.LaboratoryResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.EquipmentModel;
import br.com.ucs.laboratorio.gestao.domain.entity.EventModel;
import br.com.ucs.laboratorio.gestao.domain.entity.LaboratoryModel;
import br.com.ucs.laboratorio.gestao.application.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.domain.type.EventType;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.LaboratoryRepository;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private EquipmentDao equipmentDao;

    @Autowired
    private EventDao eventDao;

    public LaboratoryResponse create(LaboratoryDto laboratoryDto) {
        var block = blockService.findById(laboratoryDto.getBlockId());
        var laboratoryModel = modelMapper.map(laboratoryDto, LaboratoryModel.class);
        laboratoryModel.setBlock(block);
        var save = laboratoryRepository.save(laboratoryModel);
        return modelMapper.map(save, LaboratoryResponse.class);
    }

    public LaboratoryModel findById(Long id) {
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

    public LaboratoryEventTotalizerResponse totalEvents(Long laboratoryId,
                                                        Long categoryId,
                                                        EventType eventType,
                                                        LocalDate startDate,
                                                        LocalDate endDate) {

        List<EquipmentModel> equipments = equipmentDao.findEquipment(laboratoryId, categoryId, null);
        List<Long> equipmentIds = equipments.stream().map(EquipmentModel::getId).toList();

        List<EventModel> allEvents = eventDao.findEventsByEquipments(equipmentIds, eventType, startDate, endDate);

        Map<Long, List<EventTotalizerItemsResponse>> eventsByEquipmentId = allEvents.stream()
                .map(event -> MapperUtil.mapObject(event, EventTotalizerItemsResponse.class))
                .collect(Collectors.groupingBy(event -> event.getId() != null
                        ? allEvents.stream()
                        .filter(e -> e.getId().equals(event.getId()))
                        .findFirst()
                        .get()
                        .getEquipment()
                        .getId()
                        : null));

        var eventTotalizerResponses = equipments.stream()
                .map(eq ->
                        eventService.totalEvents(eq, eventsByEquipmentId.getOrDefault(eq.getId(), Collections.emptyList()))
                )
                .collect(Collectors.toList());

        eventTotalizerResponses.removeIf(event -> event.getEventItems().isEmpty());

        var total = eventTotalizerResponses.stream()
                .map(EventTotalizerResponse::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new LaboratoryEventTotalizerResponse(total, eventTotalizerResponses);
    }
}
