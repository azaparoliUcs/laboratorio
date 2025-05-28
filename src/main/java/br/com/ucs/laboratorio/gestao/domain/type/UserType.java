package br.com.ucs.laboratorio.gestao.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum UserType {

    RESPONSIBLE(1, "RESPONSAVEL"),
    TECHNICAL(2, "TECNICO");

    private final Integer code;
    private final String description;

    private static final Map<Integer, UserType> USER_CODES = Arrays.stream(UserType.values())
            .collect(Collectors.toMap(UserType::getCode, Function.identity()));

    public static UserType getUserType(Integer code){
        return USER_CODES.get(code);
    }
}
