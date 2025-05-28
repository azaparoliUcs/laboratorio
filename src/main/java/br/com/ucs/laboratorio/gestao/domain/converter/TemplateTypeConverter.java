package br.com.ucs.laboratorio.gestao.domain.converter;

import br.com.ucs.laboratorio.gestao.domain.type.TemplateType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter
public class TemplateTypeConverter implements AttributeConverter<TemplateType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TemplateType attribute) {
        return Objects.isNull(attribute) ? null : attribute.getCode();
    }

    @Override
    public TemplateType convertToEntityAttribute(Integer integer) {
        return TemplateType.getTemplateType(integer);
    }
}
