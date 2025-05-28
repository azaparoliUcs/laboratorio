package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.EquipmentDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EquipmentResponse;
import br.com.ucs.laboratorio.gestao.domain.service.EquipmentService;
import br.com.ucs.laboratorio.gestao.util.MapperUtil;
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
        return ResponseEntity.ok(MapperUtil.mapObject(equipmentService.findById(id), EquipmentResponse.class));
    }

    @GetMapping
    public ResponseEntity<List<EquipmentResponse>> findAll(){
        return ResponseEntity.ok(equipmentService.findAll());
    }
}
