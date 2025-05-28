package br.com.ucs.laboratorio.gestao.domain.dto;

import br.com.ucs.laboratorio.gestao.domain.entity.TemplateModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class EquipmentDto {

    private Long id;

    private String propertyNumber;

    private String number;

    private String equipmentTag;

    private TemplateModel template;

    private Long laboratoryId;
}
