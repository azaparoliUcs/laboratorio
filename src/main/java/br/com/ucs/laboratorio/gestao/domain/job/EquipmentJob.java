package br.com.ucs.laboratorio.gestao.domain.job;

import br.com.ucs.laboratorio.gestao.domain.type.EquipmentStatusType;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EquipmentJob {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void executeEquipmentJob() {
        var equipmentsExpired = equipmentRepository.findEquipmentsExpired(LocalDate.now().minusDays(1));

        equipmentsExpired.forEach(equip -> {
            equip.setEquipmentStatusType(EquipmentStatusType.UNAVAILABLE);
            equip.setCalibrationFlag(false);
        });

        equipmentRepository.saveAll(equipmentsExpired);
    }

}
