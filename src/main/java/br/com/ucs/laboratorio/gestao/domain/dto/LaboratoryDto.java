package br.com.ucs.laboratorio.gestao.domain.dto;

import jakarta.persistence.Column;
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

    private String roomNumber;

    private String roomName;

    private Long blockId;
}
