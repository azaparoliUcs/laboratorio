package br.com.ucs.laboratorio.gestao.domain.dto.response;

import br.com.ucs.laboratorio.gestao.domain.entity.TemplateModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class EquipmentResponse {

    private Long id;

    private String propertyNumber;

    private String number;

    private String equipmentTag;

    private TemplateModel modelo;

    private List<EventResponse> events;
}
