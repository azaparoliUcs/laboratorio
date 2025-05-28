package br.com.ucs.laboratorio.gestao.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum TemplateType {

    ANALOG(1, "ANALOGICO"),
    DIGITAL(2, "DIGITAL");

    private final Integer code;
    private final String description;

    private static final Map<Integer, TemplateType> TEMPLATE_CODES = Arrays.stream(TemplateType.values())
            .collect(Collectors.toMap(TemplateType::getCode, Function.identity()));

    public static TemplateType getTemplateType(Integer code){
        return TEMPLATE_CODES.get(code);
    }
}
