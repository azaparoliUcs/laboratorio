package br.com.ucs.laboratorio.gestao.domain.dto;

import br.com.ucs.laboratorio.gestao.domain.type.PeriodType;
import br.com.ucs.laboratorio.gestao.domain.type.TemplateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TemplateDto {

    private Long id;

    private String identification;

    private String description;

    private String brand;

    private Integer maintenancePeriod;

    private PeriodType periodType;

    private TemplateType templateType;

    private Long categoryId;
}
