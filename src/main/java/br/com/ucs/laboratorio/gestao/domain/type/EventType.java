package br.com.ucs.laboratorio.gestao.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum EventType {

    CALIBRATION(1, "CALIBRACAO"),
    MAINTENANCE(2, "MANUTENCAO"),
    QUALIFICATIONS(3, "QUALIFICACAO"),
    CHECKS(4, "CHECAGEM"),
    OTHER(5, "OUTROS");

    private final Integer code;
    private final String description;

    private static final Map<Integer, EventType> EVENT_CODES = Arrays.stream(EventType.values())
            .collect(Collectors.toMap(EventType::getCode, Function.identity()));

    public static EventType getEventType(Integer code){
        return EVENT_CODES.get(code);
    }
}
