package br.com.ucs.laboratorio.gestao.infrastructure.repository;

import br.com.ucs.laboratorio.gestao.domain.entity.EventModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventModel, Long> {
}
