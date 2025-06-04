package br.com.ucs.laboratorio.gestao.domain.entity;

import br.com.ucs.laboratorio.gestao.domain.converter.PeriodCalibrationTypeConverter;
import br.com.ucs.laboratorio.gestao.domain.converter.PeriodMaintenanceTypeConverter;
import br.com.ucs.laboratorio.gestao.domain.converter.TemplateTypeConverter;
import br.com.ucs.laboratorio.gestao.domain.type.PeriodCalibrationType;
import br.com.ucs.laboratorio.gestao.domain.type.PeriodMaintenanceType;
import br.com.ucs.laboratorio.gestao.domain.type.TemplateType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "TB_MODELO")
public class TemplateModel {

    @Id
    @Column(name = "ID_MODELO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DS_DESCRICAO")
    private String description;

    @Column(name = "DS_MARCA")
    private String brand;

    @Column(name = "TP_PERIODO_CALIBRACAO")
    @Convert(converter = PeriodCalibrationTypeConverter.class)
    private PeriodCalibrationType periodCalibrationType;

    @Column(name = "TP_PERIODO_MANUTENCAO_PREV")
    @Convert(converter = PeriodMaintenanceTypeConverter.class)
    private PeriodMaintenanceType periodMaintenanceType;

    @Column(name = "DS_CA_CALIBRACAO")
    private String calibrationCriterion;

    @Column(name = "DS_CA_VERIFICACAO")
    private String verificationCriterion;

    @Column(name = "DS_CAPACIDADE_MEDICAO")
    private String capacityMeasurement;

    @Column(name = "TP_MODELO")
    @Convert(converter = TemplateTypeConverter.class)
    private TemplateType templateType;

    @ManyToOne
    @JoinColumn(name = "ID_CATEGORIA")
    private CategoryModel category;
}
