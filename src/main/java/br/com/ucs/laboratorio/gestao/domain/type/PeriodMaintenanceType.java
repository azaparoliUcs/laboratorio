package br.com.ucs.laboratorio.gestao.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum PeriodMaintenanceType {

    ONE_MONTH(1, "1 Mês"),
    TWO_MONTHS(2, "2 Meses"),
    THREE_MONTHS(3, "3 Meses"),
    SIX_MONTHS(6, "6 Meses"),
    ONE_YEAR(12, "1 Ano"),
    ONE_AND_HALF_YEAR(18, "1 Ano e meio"),
    TWO_YEARS(24, "2 Anos");

    private final int code;
    private final String description;

    private static final Map<Integer, PeriodMaintenanceType> PERIOD_CODES = Arrays.stream(PeriodMaintenanceType.values())
            .collect(Collectors.toMap(PeriodMaintenanceType::getCode, Function.identity()));

    public static PeriodMaintenanceType getPeriodType(Integer code){
        return PERIOD_CODES.get(code);
    }
}
