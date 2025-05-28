package br.com.ucs.laboratorio.gestao.domain.entity;

import br.com.ucs.laboratorio.gestao.domain.converter.PeriodTypeConverter;
import br.com.ucs.laboratorio.gestao.domain.converter.TemplateTypeConverter;
import br.com.ucs.laboratorio.gestao.domain.type.PeriodType;
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

    @Column(name = "DS_IDENTIFICACAO")
    private String identification;

    @Column(name = "DS_DESCRICAO")
    private String description;

    @Column(name = "DS_MARCA")
    private String brand;

    @Column(name = "NR_PERIODO_MANUTENCAO")
    private Integer maintenancePeriod;

    @Column(name = "TP_PERIODO")
    @Convert(converter = PeriodTypeConverter.class)
    private PeriodType periodType;

    @Column(name = "TP_MODELO")
    @Convert(converter = TemplateTypeConverter.class)
    private TemplateType templateType;

    @ManyToOne
    @JoinColumn(name = "ID_CATEGORIA")
    private CategoryModel category;
}
