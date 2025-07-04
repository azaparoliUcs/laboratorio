package br.com.ucs.laboratorio.gestao.domain.dto.response;

import br.com.ucs.laboratorio.gestao.domain.type.EquipmentStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class EquipmentResponse {

    private Long id;

    private String identification;

    private EquipmentStatusType equipmentStatusType;

    private String description;

    private String propertyNumber;

    private String serialNumber;

    private String equipmentTag;

    private LocalDate dateOfUse;

    private LocalDate nextCalibrationDate;

    private LocalDate nextMaintenanceDate;

    private TemplateResponse template;

    private Long daysExpiration;

    private List<EventResponse> events;
}
