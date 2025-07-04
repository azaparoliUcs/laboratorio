package br.com.ucs.laboratorio.gestao.domain.dto.response;

import br.com.ucs.laboratorio.gestao.domain.type.EventStatusType;
import br.com.ucs.laboratorio.gestao.domain.type.EventType;
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
public class EventResponse {

    private Long id;

    private EventType eventType;

    private LocalDate requestDate;

    private LocalDate finalizedDate;

    private Long requestNumber;

    private Boolean calibrationRequested;

    private EventStatusType status;

    private String observation;

    private BigDecimal costValue;

    private String certificateNumber;
}
