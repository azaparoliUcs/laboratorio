package br.com.ucs.laboratorio.gestao.domain.dto;

import br.com.ucs.laboratorio.gestao.domain.type.EquipmentStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class EquipmentDto {

    private Long id;

    private String identification;

    private EquipmentStatusType equipmentStatusType;

    private String propertyNumber;

    private String serialNumber;

    private String equipmentTag;

    private LocalDate dateOfUse;

    private LocalDate nextCalibrationDate;

    private LocalDate nextMaintenanceDate;

    private Long templateId;

    private Long laboratoryId;
}
