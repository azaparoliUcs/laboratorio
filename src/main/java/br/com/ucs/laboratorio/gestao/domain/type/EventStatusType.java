package br.com.ucs.laboratorio.gestao.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum EventStatusType {

    REGISTERED(1, "Cadastrado"),
    IN_PROGRESS(2, "Em andamento"),
    FINALIZED(3, "Fianalizado");

    private final Integer code;
    private final String description;

    private static final Map<Integer, EventStatusType> EVENT_CODES = Arrays.stream(EventStatusType.values())
            .collect(Collectors.toMap(EventStatusType::getCode, Function.identity()));

    public static EventStatusType getEventStatusType(Integer code){
        return EVENT_CODES.get(code);
    }
}
