package br.com.ucs.laboratorio.gestao.domain.dto;

import br.com.ucs.laboratorio.gestao.domain.type.PeriodCalibrationType;
import br.com.ucs.laboratorio.gestao.domain.type.PeriodMaintenanceType;
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

    private PeriodCalibrationType periodCalibrationType;

    private String capacityMeasurement;

    private String verificationCriterion;

    private String calibrationCriterion;

    private PeriodMaintenanceType periodMaintenanceType;

    private TemplateType templateType;

    private Long categoryId;
}
