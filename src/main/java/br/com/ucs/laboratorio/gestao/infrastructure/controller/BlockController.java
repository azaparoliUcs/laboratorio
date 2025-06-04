package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.BlockDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.BlockResponse;
import br.com.ucs.laboratorio.gestao.domain.service.BlockService;
import br.com.ucs.laboratorio.gestao.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/block")
public class BlockController {

    @Autowired
    private BlockService blockService;

    @PostMapping
    public ResponseEntity<BlockResponse> create(@RequestBody BlockDto blockDto){
        return ResponseEntity.ok(blockService.create(blockDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlockResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(MapperUtil.mapObject(blockService.findById(id), BlockResponse.class));
    }

    @GetMapping
    public ResponseEntity<List<BlockResponse>> findAll(){
        return ResponseEntity.ok(blockService.findAll());
    }

    @PutMapping("/{id}")
    private ResponseEntity<BlockResponse> update(@PathVariable Long id, @RequestBody BlockDto blockDto){
        return ResponseEntity.ok(blockService.update(id, blockDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        blockService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
