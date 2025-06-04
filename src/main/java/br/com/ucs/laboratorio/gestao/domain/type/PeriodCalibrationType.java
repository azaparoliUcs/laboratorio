package br.com.ucs.laboratorio.gestao.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum PeriodCalibrationType {

    NONE(0.0, "Não Requer Calibração"),
    THREE_MONTHS(0.25, "3 Meses"),
    SIX_MONTHS(0.5, "6 Meses"),
    ONE_YEAR(1.0, "1 Ano"),
    ONE_AND_HALF_YEAR(1.5, "1 Ano e meio"),
    TWO_YEARS(2.0, "2 Anos"),
    THREE_YEARS(3.0, "3 Anos"),
    FOUR_YEARS(4.0, "4 Anos"),
    FIVE_YEARS(5.0, "5 Anos");

    private final Double code;
    private final String description;

    private static final Map<Double, PeriodCalibrationType> PERIOD_CODES = Arrays.stream(PeriodCalibrationType.values())
            .collect(Collectors.toMap(PeriodCalibrationType::getCode, Function.identity()));

    public static PeriodCalibrationType getPeriodType(Double code){
        return PERIOD_CODES.get(code);
    }
}
