package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import br.com.ucs.laboratorio.gestao.domain.dto.EquipmentDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EquipmentResponse;
import br.com.ucs.laboratorio.gestao.domain.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @PostMapping
    public ResponseEntity<EquipmentResponse> create(@RequestBody EquipmentDto equipmentDto){
        return ResponseEntity.ok(equipmentService.create(equipmentDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponse> findById(@PathVariable Long id){
        var model = equipmentService.findById(id);
        return ResponseEntity.ok(MapperUtil.mapObject(model, EquipmentResponse.class));
    }

    @GetMapping
    public ResponseEntity<List<EquipmentResponse>> findAll(){
        return ResponseEntity.ok(equipmentService.findAll());
    }

    @GetMapping("/laboratory/{id}")
    public ResponseEntity<List<EquipmentResponse>> findByLaboratory(@PathVariable Long id){
        return ResponseEntity.ok(equipmentService.findByLaboratoryId(id));
    }

    @GetMapping("/expiration")
    public List<EquipmentResponse> findExirationEquipment(@RequestParam(required = false) Long id) {
        return equipmentService.findExpirationEquipment(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentResponse> update(@PathVariable Long id, @RequestBody EquipmentDto equipmentDto){
        return ResponseEntity.ok(equipmentService.update(id, equipmentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        equipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
