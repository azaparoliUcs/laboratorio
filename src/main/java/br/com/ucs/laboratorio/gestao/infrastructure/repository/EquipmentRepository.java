package br.com.ucs.laboratorio.gestao.infrastructure.repository;

import br.com.ucs.laboratorio.gestao.domain.entity.EquipmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentModel, Long> {

    @Query("SELECT e FROM EquipmentModel e WHERE e.laboratory.id = :laboratoryId AND " +
            "(((e.nextCalibrationDate BETWEEN :initialDate AND :finalDate OR " +
            "e.nextCalibrationDate < :initialDate) AND e.calibrationFlag = true) OR e.calibrationFlag = true)")
    List<EquipmentModel> findExpirationByLaboratoryId(@Param("laboratoryId") Long laboratoryId,
                                                      @Param("initialDate") LocalDate initialDate,
                                                      @Param("finalDate") LocalDate finalDate);

    @Query("SELECT e FROM EquipmentModel e WHERE " +
            "((e.nextCalibrationDate BETWEEN :initialDate AND :finalDate OR " +
            "e.nextCalibrationDate < :initialDate) AND e.calibrationFlag = true) OR e.calibrationFlag = true")
    List<EquipmentModel> findExpiration(@Param("initialDate") LocalDate initialDate,
                                        @Param("finalDate") LocalDate finalDate);

    @Query("SELECT e FROM EquipmentModel e WHERE e.nextCalibrationDate = :date")
    List<EquipmentModel> findEquipmentsExpired(LocalDate date);
}
