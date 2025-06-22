package br.com.ucs.laboratorio.gestao.domain.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventTotalizerResponse {

    private Long equipmentId;

    private BigDecimal total;

    private List<EventTotalizerItemsResponse> eventItems;
}
