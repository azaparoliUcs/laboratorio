package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.TemplateDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.TemplateResponse;
import br.com.ucs.laboratorio.gestao.domain.service.TemplateService;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import br.com.ucs.laboratorio.gestao.domain.type.TemplateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @PostMapping
    public ResponseEntity<TemplateResponse> create(@RequestBody TemplateDto templateDto){
        return ResponseEntity.ok(templateService.create(templateDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(MapperUtil.mapObject(templateService.findById(id), TemplateResponse.class));
    }

    @GetMapping
    public ResponseEntity<List<TemplateResponse>> findAll(){
        return ResponseEntity.ok(templateService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemplateResponse> update(@PathVariable Long id, @RequestBody TemplateDto templateDto){
        return ResponseEntity.ok(templateService.update(id, templateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        templateService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public List<TemplateResponse> filterTemplates(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) TemplateType templateType,
            @RequestParam(required = false) Long categoryId
    ) {
        return templateService.filterTemplates(brand, templateType, categoryId);
    }
}
