package br.com.ucs.laboratorio.gestao.application.util;

import br.com.ucs.laboratorio.gestao.domain.type.PeriodCalibrationType;
import br.com.ucs.laboratorio.gestao.domain.type.PeriodMaintenanceType;

import java.time.LocalDate;

public class DateUtil {

    public static LocalDate calculateNextPeriodCalibration(PeriodCalibrationType calibrationType) {
        double years = calibrationType.getCode();

        int wholeYears = (int) years;
        double fraction = years - wholeYears;
        int month = (int) Math.round(fraction * 12);

        return LocalDate.now().plusYears(wholeYears).plusMonths(month);
    }

    public static LocalDate calculateNextPeriodMaintenance(PeriodMaintenanceType maintenanceType) {
        return LocalDate.now().plusMonths(maintenanceType.getCode());
    }
}
