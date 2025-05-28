package br.com.ucs.laboratorio.gestao.domain.dto.response;

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
public class BlockResponse implements Serializable {

    private Long id;

    private String description;

    private List<LaboratoryResponse> laboratories;
}
