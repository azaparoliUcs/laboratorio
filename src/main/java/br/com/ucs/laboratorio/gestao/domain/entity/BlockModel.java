package br.com.ucs.laboratorio.gestao.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "TB_BLOCO")
public class BlockModel implements Serializable {

    @Id
    @Column(name = "ID_BLOCO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DS_BLOCO")
    private String description;

    @OneToMany
    @JoinColumn(name = "ID_LABORATORIO")
    private List<LaboratoryModel> laboratory;
}
