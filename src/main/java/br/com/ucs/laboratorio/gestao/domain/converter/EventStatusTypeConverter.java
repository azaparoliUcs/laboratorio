package br.com.ucs.laboratorio.gestao.domain.converter;

import br.com.ucs.laboratorio.gestao.domain.type.EquipmentStatusType;
import br.com.ucs.laboratorio.gestao.domain.type.EventStatusType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter
public class EventStatusTypeConverter implements AttributeConverter<EventStatusType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EventStatusType attribute) {
        return Objects.isNull(attribute) ? null : attribute.getCode();
    }

    @Override
    public EventStatusType convertToEntityAttribute(Integer integer) {
        return EventStatusType.getEventStatusType(integer);
    }
}
