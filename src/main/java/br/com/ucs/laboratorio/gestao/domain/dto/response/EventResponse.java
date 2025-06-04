package br.com.ucs.laboratorio.gestao.domain.dto.response;

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

    private String eventType;

    private LocalDate eventDate;

    private Long requestNumber;

    private Boolean calibrationRequested;

    private String status;

    private String observation;

    private BigDecimal costValue;

    private String certificateNumber;
}
