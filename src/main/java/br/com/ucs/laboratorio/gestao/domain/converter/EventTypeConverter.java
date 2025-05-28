package br.com.ucs.laboratorio.gestao.domain.converter;

import br.com.ucs.laboratorio.gestao.domain.type.EventType;
import br.com.ucs.laboratorio.gestao.domain.type.UserType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter
public class EventTypeConverter implements AttributeConverter<EventType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EventType attribute) {
        return Objects.isNull(attribute) ? null : attribute.getCode();
    }

    @Override
    public EventType convertToEntityAttribute(Integer integer) {
        return EventType.getEventType(integer);
    }
}
