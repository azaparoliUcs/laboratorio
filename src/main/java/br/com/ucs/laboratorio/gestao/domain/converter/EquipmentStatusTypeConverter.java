package br.com.ucs.laboratorio.gestao.domain.converter;

import br.com.ucs.laboratorio.gestao.domain.type.EquipmentStatusType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter
public class EquipmentStatusTypeConverter implements AttributeConverter<EquipmentStatusType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EquipmentStatusType attribute) {
        return Objects.isNull(attribute) ? null : attribute.getCode();
    }

    @Override
    public EquipmentStatusType convertToEntityAttribute(Integer integer) {
        return EquipmentStatusType.getEquipmentStatusType(integer);
    }
}
