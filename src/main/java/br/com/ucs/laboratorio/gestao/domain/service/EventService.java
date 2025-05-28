package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.EventDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.EventModel;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.EventRepository;
import br.com.ucs.laboratorio.gestao.util.MapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private ModelMapper modelMapper;

    public EventResponse create(EventDto eventDto) {
        var equipment = equipmentService.findById(eventDto.getEquipmentId());
        var eventModel = modelMapper.map(eventDto, EventModel.class);
        eventModel.setEquipment(equipment);
        var save = eventRepository.save(eventModel);
        return modelMapper.map(save, EventResponse.class);
    }

    public EventModel findById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Evento nao existe"));
    }

    public List<EventResponse> findAll() {
        return MapperUtil.mapList(eventRepository.findAll(), EventResponse.class);
    }
}
