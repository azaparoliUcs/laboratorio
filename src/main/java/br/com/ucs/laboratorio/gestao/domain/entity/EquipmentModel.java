package br.com.ucs.laboratorio.gestao.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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

    @Column(name = "NR_PATRIMONIO")
    private String propertyNumber;

    @Column(name = "NR_NUMERO")
    private String number;

    @Column(name = "DS_TAG")
    private String equipmentTag;

    @ManyToOne
    @JoinColumn(name = "ID_MODELO")
    private TemplateModel template;

    @ManyToOne
    @JoinColumn(name = "ID_LABORATORIO")
    private LaboratoryModel laboratory;

    @OneToMany(mappedBy = "equipment")
    private List<EventModel> events;
}
