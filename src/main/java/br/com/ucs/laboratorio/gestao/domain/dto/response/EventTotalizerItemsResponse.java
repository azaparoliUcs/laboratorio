package br.com.ucs.laboratorio.gestao.domain.dto.response;

import br.com.ucs.laboratorio.gestao.domain.type.EventStatusType;
import br.com.ucs.laboratorio.gestao.domain.type.EventType;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventTotalizerItemsResponse {

    private Long id;

    private EventType eventType;

    private EventStatusType status;

    private BigDecimal costValue;
}
