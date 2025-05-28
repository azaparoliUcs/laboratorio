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
@Table(name = "TB_LABORATORIO")
public class LaboratoryModel {

    @Id
    @Column(name = "ID_LABORATORIO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DS_SALA")
    private String room;

    @OneToMany(mappedBy = "laboratory")
    private List<EquipmentModel> equipmentModels;

    @ManyToOne
    @JoinColumn(name = "ID_BLOCO")
    private BlockModel block;
}
