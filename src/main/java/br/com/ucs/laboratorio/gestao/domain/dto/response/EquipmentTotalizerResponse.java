package br.com.ucs.laboratorio.gestao.domain.dto.response;

import br.com.ucs.laboratorio.gestao.domain.type.EquipmentStatusType;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentTotalizerResponse {

    private Long id;

    private String identification;

    private EquipmentStatusType equipmentStatusType;

    private String description;

    private String propertyNumber;

    private String serialNumber;

    private String equipmentTag;

    private LocalDate dateOfUse;

    private TemplateResponse template;
}
