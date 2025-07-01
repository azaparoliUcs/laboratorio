package br.com.ucs.laboratorio.gestao.domain.entity;

import br.com.ucs.laboratorio.gestao.domain.converter.EquipmentStatusTypeConverter;
import br.com.ucs.laboratorio.gestao.domain.type.EquipmentStatusType;
import jakarta.persistence.*;
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
@Entity
@Table(name = "TB_EQUIPAMENTO")
public class EquipmentModel {

    @Id
    @Column(name = "ID_EQUIPAMENTO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CD_STATUS")
    @Convert(converter = EquipmentStatusTypeConverter.class)
    private EquipmentStatusType equipmentStatusType;

    @Column(name = "NM_DESCRIPTION")
    private String description;

    @Column(name = "DS_IDENTIFICACAO")
    private String identification;

    @Column(name = "NR_PATRIMONIO")
    private String propertyNumber;

    @Column(name = "NR_SERIE")
    private String serialNumber;

    @Column(name = "FL_CALIBRACAO")
    public Boolean calibrationFlag = false;

    @Column(name = "DS_TAG")
    private String equipmentTag;

    @Column(name = "DT_USO")
    private LocalDate dateOfUse;

    @Column(name = "DT_PROXIMA_CALIBRACAO")
    private LocalDate nextCalibrationDate;

    @Column(name = "DT_PROXIMA_MANUTENCAO")
    private LocalDate nextMaintenanceDate;

    @ManyToOne
    @JoinColumn(name = "ID_MODELO")
    private TemplateModel template;

    @ManyToOne
    @JoinColumn(name = "ID_LABORATORIO")
    private LaboratoryModel laboratory;
}
