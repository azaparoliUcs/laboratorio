package br.com.ucs.laboratorio.gestao.domain.dto.response;

import jakarta.persistence.Column;
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

    private String roomNumber;

    private String roomName;

    private List<EquipmentResponse> equipments;
}
