package br.com.ucs.laboratorio.gestao.domain.converter;

import br.com.ucs.laboratorio.gestao.domain.type.PeriodCalibrationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter
public class PeriodCalibrationTypeConverter implements AttributeConverter<PeriodCalibrationType, Double> {

    @Override
    public Double convertToDatabaseColumn(PeriodCalibrationType attribute) {
        return Objects.isNull(attribute) ? null : attribute.getCode();
    }

    @Override
    public PeriodCalibrationType convertToEntityAttribute(Double code) {
        return PeriodCalibrationType.getPeriodType(code);
    }
}
