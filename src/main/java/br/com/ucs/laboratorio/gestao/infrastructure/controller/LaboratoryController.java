package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.LaboratoryDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventTotalizerResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.LaboratoryEventTotalizerResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.LaboratoryResponse;
import br.com.ucs.laboratorio.gestao.domain.service.LaboratoryService;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import br.com.ucs.laboratorio.gestao.domain.type.EventStatusType;
import br.com.ucs.laboratorio.gestao.domain.type.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/laboratory")
public class LaboratoryController {

    @Autowired
    private LaboratoryService laboratoryService;

    @PostMapping
    public ResponseEntity<LaboratoryResponse> create(@RequestBody LaboratoryDto laboratoryDto){
        return ResponseEntity.ok(laboratoryService.create(laboratoryDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaboratoryResponse> findById(@PathVariable Long id){
        var model = laboratoryService.findById(id);
        var response = MapperUtil.mapObject(model, LaboratoryResponse.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LaboratoryResponse>> findAll(){
        return ResponseEntity.ok(laboratoryService.findAll());
    }

    @GetMapping("/total/{id}")
    public ResponseEntity<LaboratoryEventTotalizerResponse> total(@PathVariable Long id,
                                                                  @RequestParam(required = false) Long categoryId,
                                                                  @RequestParam(required = false) EventType eventType,
                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ){
        return ResponseEntity.ok(laboratoryService.totalEvents(id, categoryId, eventType, startDate, endDate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaboratoryResponse> update(@PathVariable Long id, @RequestBody LaboratoryDto laboratoryDto){
        return ResponseEntity.ok(laboratoryService.update(id, laboratoryDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        laboratoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
