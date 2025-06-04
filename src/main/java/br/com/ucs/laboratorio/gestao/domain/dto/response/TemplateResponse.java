package br.com.ucs.laboratorio.gestao.domain.dto.response;

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
public class TemplateResponse {

    private Long id;

    private String description;

    private String brand;

    private Integer maintenancePeriod;

    private PeriodCalibrationType periodCalibrationType;

    private String capacityMeasurement;

    private String verificationCriterion;

    private String calibrationCriterion;

    private PeriodMaintenanceType periodMaintenanceType;

    private TemplateType templateType;

    private CategoryResponse category;
}
