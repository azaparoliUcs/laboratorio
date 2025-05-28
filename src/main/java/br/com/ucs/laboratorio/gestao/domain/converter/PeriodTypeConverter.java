package br.com.ucs.laboratorio.gestao.domain.converter;

import br.com.ucs.laboratorio.gestao.domain.type.PeriodType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter
public class PeriodTypeConverter implements AttributeConverter<PeriodType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PeriodType attribute) {
        return Objects.isNull(attribute) ? null : attribute.getCode();
    }

    @Override
    public PeriodType convertToEntityAttribute(Integer integer) {
        return PeriodType.getPeriodType(integer);
    }
}
