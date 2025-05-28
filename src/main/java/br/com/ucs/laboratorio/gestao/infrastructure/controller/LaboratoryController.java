package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.LaboratoryDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.LaboratoryResponse;
import br.com.ucs.laboratorio.gestao.domain.service.LaboratoryService;
import br.com.ucs.laboratorio.gestao.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(MapperUtil.mapObject(laboratoryService.findById(id), LaboratoryResponse.class));
    }

    @GetMapping
    public ResponseEntity<List<LaboratoryResponse>> findAll(){
        return ResponseEntity.ok(laboratoryService.findAll());
    }
}
