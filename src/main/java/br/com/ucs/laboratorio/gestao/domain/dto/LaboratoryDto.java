package br.com.ucs.laboratorio.gestao.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class LaboratoryDto {

    private Long id;

    private String room;

    private Long blockId;
}
