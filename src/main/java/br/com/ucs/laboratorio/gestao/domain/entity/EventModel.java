package br.com.ucs.laboratorio.gestao.domain.entity;

import br.com.ucs.laboratorio.gestao.domain.converter.EventTypeConverter;
import br.com.ucs.laboratorio.gestao.domain.type.EventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "TB_EVENTO")
public class EventModel {

    @Id
    @Column(name = "ID_EVENTO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TP_TIPO_EVENTO")
    @Convert(converter = EventTypeConverter.class)
    private EventType eventType;

    @Column(name = "DT_EVENTO")
    private LocalDate eventDate;

    @Column(name = "DS_DESCRICAO")
    private String description;

    @Column(name = "VL_CUSTO")
    private BigDecimal costValue;

    @Column(name = "NR_CERTIFICADO")
    private String certificateNumber;

    @ManyToOne
    @JoinColumn(name = "ID_EQUIPAMENTO")
    private EquipmentModel equipment;
}
