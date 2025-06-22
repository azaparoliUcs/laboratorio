package br.com.ucs.laboratorio.gestao.domain.dto.response;

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

    private String propertyNumber;

    private String serialNumber;

    private String equipmentTag;

    private LocalDate dateOfUse;

    private LocalDate nextCalibrationDate;

    private LocalDate nextMaintenanceDate;

    private TemplateResponse template;

    private boolean calibrationExpiring;

    private List<EventResponse> events;
}
