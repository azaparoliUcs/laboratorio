package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.EventDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventTotalizerItemsResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventTotalizerResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.EventModel;
import br.com.ucs.laboratorio.gestao.application.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.domain.type.EventStatusType;
import br.com.ucs.laboratorio.gestao.domain.type.EventType;
import br.com.ucs.laboratorio.gestao.domain.type.PeriodCalibrationType;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.EventRepository;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    @Lazy
    private EquipmentService equipmentService;

    @Autowired
    private ModelMapper modelMapper;

    public EventResponse create(EventDto eventDto) {
        var equipment = equipmentService.findById(eventDto.getEquipmentId());
        var eventModel = modelMapper.map(eventDto, EventModel.class);
        if (eventDto.getEventType().equals(EventType.MAINTENANCE) &&
                !equipment.getTemplate().getPeriodCalibrationType().equals(PeriodCalibrationType.NONE)){
            eventModel.setCalibrationRequested(true);
        }
        eventModel.setEquipment(equipment);
        var save = eventRepository.save(eventModel);
        return modelMapper.map(save, EventResponse.class);
    }

    public EventModel findById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new BusinessException("Evento nao existe"));
    }

    public List<EventResponse> findAll() {
        return MapperUtil.mapList(eventRepository.findAll(), EventResponse.class);
    }

    public void delete(Long id) {
        eventRepository.delete(findById(id));
    }

    public EventResponse update(Long id, EventDto eventDto) {
        EventModel event = findById(id);
        if (eventDto.getEventType().equals(EventType.CALIBRATION) &&
                Objects.nonNull(eventDto.getCertificateNumber())){
            event.setCertificateNumber(eventDto.getCertificateNumber());
        }
        if (eventDto.getStatus().equals(EventStatusType.FINALIZED)){
            equipmentService.updateStatus(eventDto.getEquipmentId(), event);
            event.setStatus(EventStatusType.FINALIZED);
        }

        event.setCostValue(eventDto.getCostValue());
        event.setObservation(eventDto.getObservation());
        return MapperUtil.mapObject(eventRepository.save(event), EventResponse.class);
    }

    public void generateEventCalibration(Long id) {
        create(EventDto.builder()
                .eventType(EventType.CALIBRATION)
                .equipmentId(id)
                .eventDate(LocalDate.now())
                .status(EventStatusType.REGISTERED)
                .build());

    }

    public EventTotalizerResponse totalEvents(Long id, LocalDate initialDate, LocalDate finalDate) {
        var events = eventRepository.findEventsByEquipmentAndDate(id, initialDate, finalDate);
        var items = MapperUtil.mapList(events, EventTotalizerItemsResponse.class);
        BigDecimal total = items.stream()
                .map(EventTotalizerItemsResponse::getCostValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new EventTotalizerResponse(id, total, items);
    }
}
