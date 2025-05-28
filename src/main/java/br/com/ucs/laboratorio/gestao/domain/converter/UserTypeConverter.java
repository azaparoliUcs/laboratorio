package br.com.ucs.laboratorio.gestao.domain.converter;

import br.com.ucs.laboratorio.gestao.domain.type.UserType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter
public class UserTypeConverter implements AttributeConverter<UserType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserType attribute) {
        return Objects.isNull(attribute) ? null : attribute.getCode();
    }

    @Override
    public UserType convertToEntityAttribute(Integer integer) {
        return UserType.getUserType(integer);
    }
}
