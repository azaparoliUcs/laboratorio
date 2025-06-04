package br.com.ucs.laboratorio.gestao.domain.dto;

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

    private String propertyNumber;

    private String serialNumber;

    private String equipmentTag;

    private LocalDate dateOfUse;

    private LocalDate nextCalibrationDate;

    private LocalDate nextMaintenanceDate;

    private Long templateId;

    private Long laboratoryId;
}
