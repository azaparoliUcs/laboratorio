package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.EventDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventResponse;
import br.com.ucs.laboratorio.gestao.domain.service.EventService;
import br.com.ucs.laboratorio.gestao.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> create(@RequestBody EventDto eventDto){
        return ResponseEntity.ok(eventService.create(eventDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(MapperUtil.mapObject(eventService.findById(id), EventResponse.class));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> findAll(){
        return ResponseEntity.ok(eventService.findAll());
    }

    @PutMapping("/{id}")
    private ResponseEntity<EventResponse> update(@PathVariable Long id, @RequestBody EventDto eventDto){
        return ResponseEntity.ok(eventService.update(id, eventDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
