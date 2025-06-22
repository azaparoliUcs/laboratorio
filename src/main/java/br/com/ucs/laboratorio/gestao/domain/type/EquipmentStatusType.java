package br.com.ucs.laboratorio.gestao.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum EquipmentStatusType {

    AVAILABLE(1, "Disponivel"),
    UNAVAILABLE(2, "Indisponivel");

    private final Integer code;
    private final String description;

    private static final Map<Integer, EquipmentStatusType> EQUIPMENT_CODES = Arrays.stream(EquipmentStatusType.values())
            .collect(Collectors.toMap(EquipmentStatusType::getCode, Function.identity()));

    public static EquipmentStatusType getEquipmentStatusType(Integer code){
        return EQUIPMENT_CODES.get(code);
    }
}
