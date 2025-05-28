package br.com.ucs.laboratorio.gestao.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum PeriodType {

    DAYS(1, "DIAS"),
    MONTH(2, "MES"),
    YEAR(3, "ANO");

    private final Integer code;
    private final String description;

    private static final Map<Integer, PeriodType> PERIOD_CODES = Arrays.stream(PeriodType.values())
            .collect(Collectors.toMap(PeriodType::getCode, Function.identity()));

    public static PeriodType getPeriodType(Integer code){
        return PERIOD_CODES.get(code);
    }
}
