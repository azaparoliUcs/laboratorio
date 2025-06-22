package br.com.ucs.laboratorio.gestao.infrastructure.repository;

import br.com.ucs.laboratorio.gestao.domain.entity.EventModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventModel, Long> {

    @Query("SELECT e FROM EventModel e WHERE e.equipment.id = :id AND e.eventDate BETWEEN :initialDate AND :finalDate")
    List<EventModel> findEventsByEquipmentAndDate(Long id, LocalDate initialDate, LocalDate finalDate);
}
