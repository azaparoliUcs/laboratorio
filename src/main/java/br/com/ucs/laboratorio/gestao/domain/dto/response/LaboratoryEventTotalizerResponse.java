package br.com.ucs.laboratorio.gestao.domain.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LaboratoryEventTotalizerResponse {

    private BigDecimal total;

    private List<EventTotalizerResponse> equipmentTotalizer;
}
