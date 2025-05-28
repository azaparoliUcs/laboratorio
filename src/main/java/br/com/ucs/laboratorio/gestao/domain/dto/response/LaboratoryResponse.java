package br.com.ucs.laboratorio.gestao.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class LaboratoryResponse {

    private Long id;

    private String room;

    private List<EquipmentResponse> equipments;
}
