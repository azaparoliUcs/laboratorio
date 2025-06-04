package br.com.ucs.laboratorio.gestao.domain.converter;

import br.com.ucs.laboratorio.gestao.domain.type.PeriodMaintenanceType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter
public class PeriodMaintenanceTypeConverter  implements AttributeConverter<PeriodMaintenanceType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PeriodMaintenanceType attribute) {
        return Objects.isNull(attribute) ? null : attribute.getCode();
    }

    @Override
    public PeriodMaintenanceType convertToEntityAttribute(Integer code) {
        return PeriodMaintenanceType.getPeriodType(code);
    }
}